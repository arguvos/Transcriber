package com.arguvos.transcriber.api;

import com.arguvos.transcriber.model.Record;
import com.arguvos.transcriber.service.RecognizeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

import static com.arguvos.transcriber.config.AppConstant.*;

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
    public String recognize(Principal principal, @RequestParam("file") MultipartFile file, Model model) {
        log.info("Initialize new recognize");
        Record record = recognizeService.createRecord(principal.getName(), file);
        model.addAttribute(RECORD_ATTRIBUTE, recognizeService.getRecord(record.getId()));
        return RECORD_PAGE;
    }

    @GetMapping(value = "/{recordId}")
    public String getRecord(@PathVariable Integer recordId, Model model) {
        model.addAttribute(RECORD_ATTRIBUTE, recognizeService.getRecord(recordId));
        return RECORD_PAGE;
    }
}
