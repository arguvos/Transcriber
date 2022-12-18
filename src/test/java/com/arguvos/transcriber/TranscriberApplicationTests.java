package com.arguvos.transcriber;

import com.arguvos.transcriber.model.Record;
import com.arguvos.transcriber.model.Role;
import com.arguvos.transcriber.model.UserEntity;
import com.arguvos.transcriber.repository.RecognizeRepository;
import com.arguvos.transcriber.repository.UserRepository;
import com.arguvos.transcriber.service.HistoryService;
import com.arguvos.transcriber.service.model.History;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
class TranscriberApplicationTests {
	private final static String DEFAULT_PASSWORD = "password";

	@Autowired
	HistoryService historyService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	RecognizeRepository recognizeRepository;

	@Test
	void testGetHistory() {
		String username = UUID.randomUUID().toString();
		userRepository.save(createUser(username));
		Long userId = userRepository.findByUsername(username).getId();

		recognizeRepository.save(createRecord(userId));
		recognizeRepository.save(createRecord(userId));
		recognizeRepository.save(createRecord(userId));

		List<History> histories = historyService.getHistoryByUser(username);
		assert histories != null;
	}

	private static UserEntity createUser(String username) {
		return UserEntity.builder()
				.username(username)
				.email(createEmailByUsername(username))
				.password(DEFAULT_PASSWORD)
				.active(true)
				.roles(Collections.singleton(Role.USER)).build();
	}

	private static String createEmailByUsername(String username) {
		return username + "@mail.com";
	}

	private static Record createRecord(Long userid) {
		Record record = new Record(UUID.randomUUID().toString(), new Random().nextLong());
		record.setUserId(userid);
		return record;
	}
}
