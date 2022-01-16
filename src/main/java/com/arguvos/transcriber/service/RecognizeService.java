package com.arguvos.transcriber.service;

import com.arguvos.transcriber.model.Record;
import com.arguvos.transcriber.repository.RecognizeRepository;
import com.arguvos.transcriber.service.ffmpeg.FfmpegClient;
import com.arguvos.transcriber.service.filestorage.FileSystemStorageService;
import com.arguvos.transcriber.service.filestorage.StorageException;
import com.arguvos.transcriber.service.vosk.VoskClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class RecognizeService {
    private final RecognizeRepository recognizeRepository;
    private final FileSystemStorageService storageService;
    private final VoskClientFactory voskClientFactory;
    private final FfmpegClient ffmpegClient;

    @Autowired
    public RecognizeService(RecognizeRepository recognizeRepository,
                            FileSystemStorageService storageService,
                            VoskClientFactory voskClientFactory,
                            FfmpegClient ffmpegClient) {
        this.recognizeRepository = recognizeRepository;
        this.storageService = storageService;
        this.voskClientFactory = voskClientFactory;
        this.ffmpegClient = ffmpegClient;
    }

    public Record createRecordNew(MultipartFile file) {
        Record record = recognizeRepository.save(new Record(file.getOriginalFilename(), file.getSize()));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                saveToDisk(file, record);
                convert(record);
                transcribe(record);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return record;
    }

    private void saveToDisk(MultipartFile file, Record record) {
        updateProgressStep(record, Record.ProgressStep.SAVE);
        try {
            String fileNameInStorage = storageService.storeWithUniqueName(file);
            record.setStoredFileName(fileNameInStorage);
            recognizeRepository.save(record);
        } catch (StorageException storageException) {
            updateStatus(record, Record.Status.FAIL);
            throw new RuntimeException();
        }
    }

    private void convert(Record record) {
        updateProgressStep(record, Record.ProgressStep.CONVERT);
        try {
            String convertedFileName = ffmpegClient.convert(record.getStoredFileName());
            record.setConvertedFileName(convertedFileName);
            recognizeRepository.save(record);
        } catch (StorageException storageException) {
            updateStatus(record, Record.Status.FAIL);
            throw new RuntimeException();
        }
    }

    private void transcribe(Record record) throws Exception {
        updateProgressStep(record, Record.ProgressStep.TRANSCRIBE);
        try {
            String result = voskClientFactory.getClient().transcribe(record.getConvertedFileName());
            log.info(result);
            record.setData(result);
            record.setStatus(Record.Status.SUCCESS);
            record.setProgressStep(Record.ProgressStep.NONE);
            recognizeRepository.save(record);
        } catch (StorageException storageException) {
            updateStatus(record, Record.Status.FAIL);
            throw new RuntimeException();
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
