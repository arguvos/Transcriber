package com.arguvos.transcriber.service;

import com.arguvos.transcriber.model.Record;
import com.arguvos.transcriber.model.UserEntity;
import com.arguvos.transcriber.repository.RecognizeRepository;
import com.arguvos.transcriber.repository.UserRepository;
import com.arguvos.transcriber.service.ffmpeg.FfmpegException;
import com.arguvos.transcriber.service.ffmpeg.FfmpegService;
import com.arguvos.transcriber.service.filestorage.FileStorageService;
import com.arguvos.transcriber.service.filestorage.StorageException;
import com.arguvos.transcriber.service.vosk.TranscribeException;
import com.arguvos.transcriber.service.vosk.TranscribeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class RecognizeService {
    private final UserRepository userRepository;
    private final RecognizeRepository recognizeRepository;
    private final FileStorageService storageService;
    private final FfmpegService ffmpegService;
    private final TranscribeService transcribeService;

    @Autowired
    public RecognizeService(UserRepository userRepository,
                            RecognizeRepository recognizeRepository,
                            FileStorageService storageService,
                            FfmpegService ffmpegService,
                            TranscribeService transcribeService) {
        this.userRepository = userRepository;
        this.recognizeRepository = recognizeRepository;
        this.storageService = storageService;
        this.transcribeService = transcribeService;
        this.ffmpegService = ffmpegService;
    }

    public Record createRecord(String username, MultipartFile file) {
        UserEntity user = userRepository.findByUsername(username);
        Long userId = user != null ? user.getId() : null;
        Record record = recognizeRepository.save(new Record(userId, file.getOriginalFilename(), file.getSize()));
        saveToDisk(file, record);
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                convert(record);
                transcribe(record);
            } catch (Exception e) {
                log.error("Fail to recognize recordId {} with error: {}", record.getId(), e.getMessage());
            }
        });
        return record;
    }

    private void saveToDisk(MultipartFile file, Record record) {
        log.debug("Save to disk file for recordId {}", record.getId());
        updateProgressStep(record, Record.ProgressStep.SAVE);
        try {
            String fileNameInStorage = storageService.storeWithUniqueName(file);
            record.setStoredFileName(fileNameInStorage);
            recognizeRepository.save(record);
            log.debug("File for recordId {} saved to disk with name {}", record.getId(), record.getStoredFileName());
        } catch (StorageException storageException) {
            log.error("Fail to save file for recordId {} with error: {}", record.getId(), storageException.getMessage());
            updateStatus(record, Record.Status.FAIL);
            throw storageException;
        }
    }

    private void convert(Record record) {
        log.debug("Convert file for recordId {}", record.getId());
        updateProgressStep(record, Record.ProgressStep.CONVERT);
        try {
            String convertedFileName = ffmpegService.convert(record.getStoredFileName());
            record.setConvertedFileName(convertedFileName);
            recognizeRepository.save(record);
            log.debug("File for recordId {} converted and saved with name {}", record.getId(), record.getConvertedFileName());
        } catch (FfmpegException|StorageException exception) {
            log.error("Fail to convert file for recordId {}", record.getId());
            updateStatus(record, Record.Status.FAIL);
            throw exception;
        }
    }

    private void transcribe(Record record) {
        log.debug("Transcribe for recordId {}", record.getId());
        updateProgressStep(record, Record.ProgressStep.TRANSCRIBE);
        try {
            String result = transcribeService.transcribe(record.getConvertedFileName());
            record.setTranscript(result);
            record.setStatus(Record.Status.SUCCESS);
            record.setProgressStep(Record.ProgressStep.NONE);
            recognizeRepository.save(record);
            log.debug("Transcribe for recordId {} successfully completer", record.getId());
        } catch (TranscribeException transcribeException) {
            log.error("Fail to transcribe for recordId {}", record.getId());
            updateStatus(record, Record.Status.FAIL);
            throw transcribeException;
        }
    }

    private void updateProgressStep(Record record, Record.ProgressStep progressStep) {
        record.setProgressStep(progressStep);
        recognizeRepository.save(record);
    }

    private void updateStatus(Record record, Record.Status status) {
        record.setStatus(status);
        recognizeRepository.save(record);
    }

    public Record getRecord(Integer recordId) {
        Optional<Record> record = recognizeRepository.findById(recordId);
        if (!record.isPresent()) {
            throw new RuntimeException();
        }
        return record.get();
    }
}
