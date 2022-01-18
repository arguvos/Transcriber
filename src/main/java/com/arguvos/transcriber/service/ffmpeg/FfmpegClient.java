package com.arguvos.transcriber.service.ffmpeg;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;

@Slf4j
@Component
public class FfmpegClient {
    private static final String URL_PATH_CONVERT = "/convert/audio/to/wav";
    @Value("${ffmpeg.server.url}")
    private String ffmpegServerUrl;
    @Value("${ffmpeg.server.port}")
    private Integer ffmpegServerPort;

    public InputStream convert(File file) {
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            HttpPost post = new HttpPost("http://" + ffmpegServerUrl + ":" + ffmpegServerPort + URL_PATH_CONVERT);
            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addPart("file", new FileBody(file))
                    .build();
            post.setEntity(reqEntity);

            try (CloseableHttpResponse response = client.execute(post)) {
                return IOUtils.toBufferedInputStream(response.getEntity().getContent());
            }
        } catch (IOException e) {
            log.error("Fail execute request to ffmpeg with error:", e);
            throw new FfmpegException("Fail execute request to ffmpeg", e);
        }
    }
}
