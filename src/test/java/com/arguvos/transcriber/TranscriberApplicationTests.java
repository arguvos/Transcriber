package com.arguvos.transcriber;

import com.arguvos.transcriber.model.Record;
import com.arguvos.transcriber.repository.RecognizeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;
import java.util.UUID;

@SpringBootTest
class TranscriberApplicationTests {

	@Autowired
	RecognizeRepository recognizeRepository;

	@Test
	void contextLoads() {
		recognizeRepository.save(createRecord());
		recognizeRepository.save(createRecord());
		recognizeRepository.save(createRecord());
		recognizeRepository.findByUserId(100L);
	}

	private Record createRecord() {
		Record record = new Record(UUID.randomUUID().toString(), new Random().nextLong());
		record.setUserId(100L);
		return record;
	}

}
