package com.arguvos.transcriber.repository;

import com.arguvos.transcriber.model.Record;
import org.springframework.data.repository.CrudRepository;

public interface RecognizeRepository extends CrudRepository<Record, Integer> {
}
