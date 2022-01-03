package com.arguvos.transcriber.service;

import com.arguvos.transcriber.model.Record;
import com.arguvos.transcriber.repository.RecognizeRepository;
import com.arguvos.transcriber.service.filestorage.FileSystemStorageService;
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

    @Autowired
    public RecognizeService(RecognizeRepository recognizeRepository,
                            FileSystemStorageService storageService,
                            VoskClientFactory voskClientFactory) {
        this.recognizeRepository = recognizeRepository;
        this.storageService = storageService;
        this.voskClientFactory = voskClientFactory;
    }

    public Record createRecord(MultipartFile file) {
        String fileNameInStorage = storageService.storeWithUniqueName(file);
        Record record = recognizeRepository.save(new Record(file.getOriginalFilename(), fileNameInStorage, file.getSize()));

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                String result = voskClientFactory.getClient().transcribe(fileNameInStorage);
                log.info(result);
                record.setData(result);
                recognizeRepository.save(record);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return record;
    }

    public Record getRecord(Integer recordId) {
        Optional<Record> record = recognizeRepository.findById(recordId);
        if (!record.isPresent()) {
            throw new RuntimeException();
        }
        return record.get();
    }
}
