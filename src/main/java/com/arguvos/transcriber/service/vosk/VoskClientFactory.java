package com.arguvos.transcriber.service.vosk;

import com.arguvos.transcriber.service.filestorage.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class VoskClientFactory {
    private final Path rootLocation;
    @Value("${vosk.server.url}")
    private String voskServerUrl;
    @Value("${vosk.server.port}")
    private Integer voskServerPort;

    @Autowired
    public VoskClientFactory(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    public VoskClient getClient() {
        return new VoskClient(voskServerUrl, voskServerPort, rootLocation);
    }
}
