package com.arguvos.transcriber.endpoint;

import com.arguvos.transcriber.model.Record;
import com.arguvos.transcriber.service.RecognizeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping(value = "/recognize")
public class RecognizeEndpoint {

    private final RecognizeService recognizeService;

    @Autowired
    public RecognizeEndpoint(RecognizeService recognizeService) {
        this.recognizeService = recognizeService;
    }

    @PostMapping
    public Integer recognize(@RequestParam("file") MultipartFile file) {
        log.info("Create new recognize");
        Record record = recognizeService.createRecord();
        return record.getId();
    }

    @GetMapping(value = "/{recordId}")
    public ResponseEntity<Record> getRecord(@PathVariable Integer recordId) {
        log.info("Get record for {}", recordId);
        try {
            return new ResponseEntity<>(recognizeService.getRecord(recordId), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Fail to get record for {}", recordId);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
