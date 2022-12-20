package com.arguvos.transcriber.service.vosk;

import com.arguvos.transcriber.service.filestorage.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
@Service
public class TranscribeService {
    private final VoskClientFactory voskClientFactory;
    private final FileStorageService storageService;

    public TranscribeService(VoskClientFactory voskClientFactory,
                             FileStorageService storageService) {
        this.voskClientFactory = voskClientFactory;
        this.storageService = storageService;
    }

    public String transcribe(String convertedFileName) {
        try {
            FileInputStream fis = new FileInputStream(storageService.getFile(convertedFileName));
            DataInputStream dis = new DataInputStream(fis);
            String result = voskClientFactory.getClient().transcribe(dis);
            fis.close();
            dis.close();
            return result;
        } catch (IOException e) {
            log.error("Fail to transcribe data by vosk with error:", e);
            throw new TranscribeException("Fail to transcribe data by vosk", e);
        }
    }

    public boolean healthcheck() {
        return voskClientFactory.getClient().healthcheck();
    }
}
