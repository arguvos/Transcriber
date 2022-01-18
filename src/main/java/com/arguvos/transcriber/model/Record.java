package com.arguvos.transcriber.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String originalFileName;
    private String storedFileName;
    private String convertedFileName;
    private Long fileSize;
    @Lob
    private String transcript;
    private Status status = Status.PROGRESS;
    private ProgressStep progressStep = ProgressStep.NONE;
    private OffsetDateTime createDate = OffsetDateTime.now();

    public enum Status {
        PROGRESS,
        SUCCESS,
        FAIL
    }

    public enum ProgressStep {
        NONE,
        SAVE,
        CONVERT,
        TRANSCRIBE
    }

    public Record(String originalFileName, Long fileSize) {
        this.originalFileName = originalFileName;
        this.fileSize = fileSize;
    }
}
