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
    private String fileName;
    private Long fileSize;
    @Lob
    private String data;
    private Status status = Status.PROGRESS;
    private OffsetDateTime createDate = OffsetDateTime.now();

    public enum Status {
        PROGRESS,
        SUCCESS,
        FAIL
    }

    public Record(String originalFileName, String fileName, Long fileSize) {
        this.originalFileName = originalFileName;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }
}
