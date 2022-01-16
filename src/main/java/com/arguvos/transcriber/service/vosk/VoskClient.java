package com.arguvos.transcriber.service.vosk;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFactory;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;

public class VoskClient {
    private final String voskServerUrl;
    private final Integer voskServerPort;
    private final Path rootLocation;

    String results = "";
    CountDownLatch recieveLatch;

    public VoskClient(String voskServerUrl, Integer voskServerPort, Path rootLocation) {
        this.voskServerUrl = voskServerUrl;
        this.voskServerPort = voskServerPort;
        this.rootLocation = rootLocation;
    }


    public synchronized String transcribe(String fileName) throws Exception {
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

        FileInputStream fis = new FileInputStream(rootLocation.resolve(fileName).toFile());
        DataInputStream dis = new DataInputStream(fis);
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

        fis.close();
        dis.close();
        return results;
    }
}
