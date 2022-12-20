package com.arguvos.transcriber.api;

import com.arguvos.transcriber.model.Record;
import com.arguvos.transcriber.service.RecognizeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.arguvos.transcriber.config.AppConstant.*;

@Slf4j
@Controller
@RequestMapping(value = DEMO_PAGE)
public class DemoEndpoint {
    private final RecognizeService recognizeService;

    @Autowired
    public DemoEndpoint(RecognizeService recognizeService) {
        this.recognizeService = recognizeService;
    }

    @PostMapping
    public String demo(@RequestParam("file") MultipartFile file, Model model) {
        log.info("Initialize new demo recognize");
        Record record = recognizeService.createRecord(DEMO_USER, file);
        model.addAttribute("record", recognizeService.getRecord(record.getId()));
        return RECORD_PAGE;
    }
}
