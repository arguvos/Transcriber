package com.arguvos.transcriber.repository;

import com.arguvos.transcriber.model.Record;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecognizeRepository extends CrudRepository<Record, Integer> {
    List<Record> findByUserId(Long userId);
}
