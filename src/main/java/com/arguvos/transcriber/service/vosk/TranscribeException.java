package com.arguvos.transcriber.service.vosk;

public class TranscribeException extends RuntimeException {

    public TranscribeException(String message) {
        super(message);
    }

    public TranscribeException(String message, Throwable cause) {
        super(message, cause);
    }
}
