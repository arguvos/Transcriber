package com.arguvos.transcriber.service.ffmpeg;

import com.arguvos.transcriber.service.filestorage.FileSystemStorageService;
import com.arguvos.transcriber.service.filestorage.StorageProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Component
public class FfmpegClient {
    private static final String PATH_CONVERT = "/convert/audio/to/wav";
    private final Path rootLocation;
    @Value("${ffmpeg.server.url}")
    private String ffmpegServerUrl;
    @Value("${ffmpeg.server.port}")
    private Integer ffmpegServerPort;
    private FileSystemStorageService fileSystemStorageService;
    @Autowired
    public FfmpegClient(StorageProperties properties,
                        FileSystemStorageService fileSystemStorageService) {
        this.rootLocation = Paths.get(properties.getLocation());
        this.fileSystemStorageService = fileSystemStorageService;
    }

    public String convert(String fileName) {
        String convertedFileName = UUID.randomUUID().toString();
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost("http://" + ffmpegServerUrl + ":" + ffmpegServerPort + PATH_CONVERT);
            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addPart("file", new FileBody(rootLocation.resolve(fileName).toFile()))
                    .build();
            post.setEntity(reqEntity);

            try (CloseableHttpResponse response = client.execute(post)) {
                log.info(response.toString());
                fileSystemStorageService.storeWithName(response.getEntity().getContent(), convertedFileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convertedFileName;
    }
}
