package com.arguvos.transcriber.service;

import com.arguvos.transcriber.model.Record;
import com.arguvos.transcriber.model.UserEntity;
import com.arguvos.transcriber.repository.RecognizeRepository;
import com.arguvos.transcriber.repository.UserRepository;
import com.arguvos.transcriber.service.model.History;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HistoryService {
    private final RecognizeRepository recognizeRepository;
    private final UserRepository userRepository;

    public HistoryService(RecognizeRepository recognizeRepository,
                          UserRepository userRepository) {
        this.recognizeRepository = recognizeRepository;
        this.userRepository = userRepository;
    }

    public List<History> getHistoryByUser(String username) {
        UserEntity user = userRepository.findByUsername(username);
        List<Record> records = recognizeRepository.findByUserId(user.getId());
        return records.stream().map(record -> new History(record.getId(),
                record.getOriginalFileName(),
                record.getFileSize(),
                record.getStatus(),
                record.getProgressStep(),
                record.getCreateDate())).collect(Collectors.toList());
    }
}
