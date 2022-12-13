package com.arguvos.transcriber.service;

import com.arguvos.transcriber.model.Record;
import com.arguvos.transcriber.repository.RecognizeRepository;
import com.arguvos.transcriber.repository.UserRepository;
import com.arguvos.transcriber.service.model.History;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

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
        //userRepository.findByEmail()
        return Arrays.asList(new History(0, "", 0L, Record.Status.SUCCESS, Record.ProgressStep.TRANSCRIBE, OffsetDateTime.now()),
                new History(0, "", 0L, Record.Status.SUCCESS, Record.ProgressStep.TRANSCRIBE, OffsetDateTime.now()));
    }
}
