package com.arguvos.transcriber.service.ffmpeg;

import com.arguvos.transcriber.service.filestorage.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Service
public class FfmpegService {
    private final FileStorageService fileSystemStorageService;
    private final FfmpegClient ffmpegClient;

    @Autowired
    public FfmpegService(FileStorageService fileSystemStorageService,
                         FfmpegClient ffmpegClient) {
        this.fileSystemStorageService = fileSystemStorageService;
        this.ffmpegClient = ffmpegClient;
    }

    public String convert(String fileName) {
        String convertedFileName = UUID.randomUUID().toString();
        InputStream convertedFileInputStream = ffmpegClient.convert(fileSystemStorageService.getFile(fileName));
        fileSystemStorageService.storeWithName(convertedFileInputStream, convertedFileName);
        return convertedFileName;
    }

    public boolean healthcheck() {
        return ffmpegClient.healthcheck();
    }
}
