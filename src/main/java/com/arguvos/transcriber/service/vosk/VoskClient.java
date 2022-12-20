package com.arguvos.transcriber.service.vosk;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class VoskClient {
    private final String voskServerUrl;
    private final Integer voskServerPort;

    String results = "";
    CountDownLatch recieveLatch;

    public VoskClient(String voskServerUrl, Integer voskServerPort) {
        this.voskServerUrl = voskServerUrl;
        this.voskServerPort = voskServerPort;
    }

    public synchronized String transcribe(DataInputStream dis) {
        try {
            WebSocketFactory factory = new WebSocketFactory();
            WebSocket ws = factory.createSocket("ws://" + voskServerUrl + ":" + voskServerPort);
            ws.addListener(new WebSocketAdapter() {
                @Override
                public void onTextMessage(WebSocket websocket, String message) {
                    results = message;
                    recieveLatch.countDown();
                }
            });
            ws.connect();

            byte[] buf = new byte[8000];
            while (true) {
                int nbytes = dis.read(buf);
                if (nbytes < 0) break;
                recieveLatch = new CountDownLatch(1);
                ws.sendBinary(buf);
                recieveLatch.await();
            }
            recieveLatch = new CountDownLatch(1);
            ws.sendText("{\"eof\" : 1}");
            recieveLatch.await();
            ws.disconnect();
            return results;
        } catch (WebSocketException|IOException|InterruptedException e) {
            log.error("Fail to transcribe data by vosk with error:", e);
            throw new TranscribeException("Fail to transcribe data by vosk");
        }
    }

    public boolean healthcheck() {
        return false;
    }
}
