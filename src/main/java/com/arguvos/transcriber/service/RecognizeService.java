package com.arguvos.transcriber.service;

import com.arguvos.transcriber.model.Record;
import com.arguvos.transcriber.repository.RecognizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class RecognizeService {
    private final RecognizeRepository recognizeRepository;

    @Autowired
    public RecognizeService(RecognizeRepository recognizeRepository) {
        this.recognizeRepository = recognizeRepository;
    }

    public Record createRecord() {
        return recognizeRepository.save(new Record(UUID.randomUUID().toString(), 0));
    }

    public Record getRecord(Integer recordId) {
        Optional<Record> record = recognizeRepository.findById(recordId);
        if (!record.isPresent()) {
            throw new RuntimeException();
        }
        return record.get();
    }
}
