package com.arguvos.transcriber.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Status {
    private String serviceName;
    private Boolean isOk;
}
