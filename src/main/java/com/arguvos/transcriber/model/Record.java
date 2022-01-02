package com.arguvos.transcriber.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.OffsetDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String fileName;
    private Integer fileSize;
    private String data;
    private Status status = Status.PROGRESS;
    private OffsetDateTime createDate = OffsetDateTime.now();

    public enum Status {
        PROGRESS,
        SUCCESS,
        FAIL
    }

    public Record(String fileName, Integer fileSize) {
        this.fileName = fileName;
        this.fileSize = fileSize;
    }
}
