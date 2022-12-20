package com.arguvos.transcriber;

import com.arguvos.transcriber.model.Role;
import com.arguvos.transcriber.model.UserEntity;
import com.arguvos.transcriber.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication
public class TranscriberApplication  implements CommandLineRunner {
	@Autowired
	UserRepository userRepository;
	@Autowired
	PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(TranscriberApplication.class, args);
	}

	@Override
	public void run(String... args) {
		if (!hasInitParameter(args)) {
			return;
		}
		UserEntity admin = UserEntity.builder()
				.username("admin")
				.email("admin@mail.com")
				.password(passwordEncoder.encode("admin"))
				.active(true)
				.roles(Collections.singleton(Role.ADMIN)).build();
		UserEntity user = UserEntity.builder()
				.username("user")
				.email("user@mail.com")
				.password(passwordEncoder.encode("user"))
				.active(true)
				.roles(Collections.singleton(Role.USER)).build();
		userRepository.saveAll(Arrays.asList(admin, user));
	}

	private static boolean hasInitParameter(String[] args) {
		if (args == null || args.length == 0) {
			return false;
		}
		return Arrays.asList(args).contains("init");
	}
}
