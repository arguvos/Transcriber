package com.arguvos.transcriber.service.vosk;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class VoskClientFactory {
    @Value("${vosk.server.url}")
    private String voskServerUrl;
    @Value("${vosk.server.port}")
    private Integer voskServerPort;

    public VoskClient getClient() {
        return new VoskClient(voskServerUrl, voskServerPort);
    }
}
