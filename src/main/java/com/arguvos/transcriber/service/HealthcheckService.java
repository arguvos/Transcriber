package com.arguvos.transcriber.service;

import com.arguvos.transcriber.service.ffmpeg.FfmpegService;
import com.arguvos.transcriber.service.model.Status;
import com.arguvos.transcriber.service.vosk.TranscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HealthcheckService {
    private static final String FFMPEG_SERVICE = "ffmpeg";
    private static final String TRANSCRIBE_SERVICE = "transcribe";

    private final FfmpegService ffmpegService;
    private final TranscribeService transcribeService;

    @Autowired
    public HealthcheckService(FfmpegService ffmpegService,
                              TranscribeService transcribeService) {
        this.ffmpegService = ffmpegService;
        this.transcribeService = transcribeService;
    }

    public List<Status> healthcheck() {
        List<Status> healthcheck = new ArrayList<>();
        healthcheck.add(new Status(FFMPEG_SERVICE, ffmpegService.healthcheck()));
        healthcheck.add(new Status(TRANSCRIBE_SERVICE, transcribeService.healthcheck()));
        return healthcheck;
    }
}
