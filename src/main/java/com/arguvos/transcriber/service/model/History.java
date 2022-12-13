package com.arguvos.transcriber.service.model;

import com.arguvos.transcriber.model.Record;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class History {
    private Integer id;

    private String originalFileName;
    private Long fileSize;
    private Record.Status status;
    private Record.ProgressStep progressStep;
    private OffsetDateTime createDate;
}
