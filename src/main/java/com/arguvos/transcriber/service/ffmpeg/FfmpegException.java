package com.arguvos.transcriber.service.ffmpeg;

public class FfmpegException extends RuntimeException {

    public FfmpegException(String message) {
        super(message);
    }

    public FfmpegException(String message, Throwable cause) {
        super(message, cause);
    }
}
