package com.arguvos.transcriber.api;

import com.arguvos.transcriber.model.Record;
import com.arguvos.transcriber.service.RecognizeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.arguvos.transcriber.config.AppConstant.*;

@Slf4j
@Controller
@RequestMapping(value = DEMO_PAGE)
public class DemoEndpoint {
    private static final int BYTE_IN_MEGABYTE = 1000000;
    private final RecognizeService recognizeService;
    private final long maxFileSize;

    @Autowired
    public DemoEndpoint(RecognizeService recognizeService,
                        @Value("${demo.max-file-size:10}") long maxFileSize) {
        this.recognizeService = recognizeService;
        this.maxFileSize = maxFileSize;
    }

    @PostMapping
    public String demo(@RequestParam("file") MultipartFile file, Model model) {
        if (maxFileSize < byteToMb(file.getSize())) {
            return INDEX_PAGE;
        }
        log.info("Initialize new demo recognize");
        Record record = recognizeService.createRecord(DEMO_USER, file);
        model.addAttribute(RECORD_ATTRIBUTE, recognizeService.getRecord(record.getId()));
        return RECORD_PAGE;
    }

    private static long byteToMb(long sizeByte) {
        return sizeByte / BYTE_IN_MEGABYTE;
    }
}
