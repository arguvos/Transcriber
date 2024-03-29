package com.arguvos.transcriber.repository;

import com.arguvos.transcriber.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long > {
    UserEntity findByEmail(String email);
    UserEntity findByUsername(String username);
}