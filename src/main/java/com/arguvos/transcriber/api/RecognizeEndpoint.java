package com.arguvos.transcriber.api;

import com.arguvos.transcriber.model.Record;
import com.arguvos.transcriber.service.RecognizeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.arguvos.transcriber.config.AppConstant.RECOGNIZE_PAGE;

@Slf4j
@Controller
@RequestMapping(value = RECOGNIZE_PAGE)
public class RecognizeEndpoint {

    private final RecognizeService recognizeService;

    @Autowired
    public RecognizeEndpoint(RecognizeService recognizeService) {
        this.recognizeService = recognizeService;
    }

    @GetMapping
    public String recognize() {
        return RECOGNIZE_PAGE;
    }

    @PostMapping
    public Integer recognize(@RequestParam("file") MultipartFile file) {
        log.info("Initialize new recognize");
        Record record = recognizeService.createRecord(file);
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
