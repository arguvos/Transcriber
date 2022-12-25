package com.arguvos.transcriber.utils;

import com.arguvos.transcriber.model.Record;
import com.arguvos.transcriber.model.Role;
import com.arguvos.transcriber.model.UserEntity;

import java.util.Collections;
import java.util.Random;
import java.util.UUID;

public class Helper {
    private final static String DEFAULT_PASSWORD = "password";

    public static UserEntity createUser(String username) {
        return UserEntity.builder()
                .username(username)
                .email(createEmailByUsername(username))
                .password(DEFAULT_PASSWORD)
                .active(true)
                .roles(Collections.singleton(Role.USER)).build();
    }

    public static String createEmailByUsername(String username) {
        return username + "@mail.com";
    }

    public static Record createRecord(Long userid) {
        Record record = new Record(UUID.randomUUID().toString(), new Random().nextLong());
        record.setUserId(userid);
        return record;
    }
}
