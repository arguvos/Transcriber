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
    private final FfmpegService ffmpegService;
    private final TranscribeService transcribeService;

    @Autowired
    public HealthcheckService(FfmpegService ffmpegService,
                              TranscribeService transcribeService) {
        this.transcribeService = transcribeService;
        this.ffmpegService = ffmpegService;
    }

    public List<Status> healthcheck() {
        List<Status> healthcheck = new ArrayList<>();
        healthcheck.add(new Status("ffmpeg", ffmpegService.healthcheck()));
        healthcheck.add(new Status("transcribe", transcribeService.healthcheck()));
        return healthcheck;
    }
}
