package com.arguvos.transcriber.service;


import com.arguvos.transcriber.exception.UserAlreadyExistException;
import com.arguvos.transcriber.model.Role;
import com.arguvos.transcriber.model.UserEntity;
import com.arguvos.transcriber.repository.UserRepository;
import com.arguvos.transcriber.service.model.UserData;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void register(UserData user) throws UserAlreadyExistException {
        if(checkIfUserExist(user.getEmail(), user.getUsername())){
            throw new UserAlreadyExistException("User already exists for this email");
        }
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);
        userEntity.setActive(true);
        userEntity.setRoles(Collections.singleton(Role.USER));
        encodePassword(userEntity, user);
        userRepository.save(userEntity);
    }

    public void updateEmail(String username, String email) {
        UserEntity userEntity = userRepository.findByUsername(username);
        if(userEntity == null){
            throw new RuntimeException("User with name " + username + " not exist");
        }
        userEntity.setEmail(email);
        userRepository.save(userEntity);
    }

    public void updatePassword(String username, String password) {
        UserEntity userEntity = userRepository.findByUsername(username);
        if(userEntity == null){
            throw new RuntimeException("User with name " + username + " not exist");
        }
        userEntity.setPassword(passwordEncoder.encode(password));
        userRepository.save(userEntity);
    }

    private boolean checkIfUserExist(String email, String username) {
        return userRepository.findByEmail(email) != null || userRepository.findByUsername(username) != null;
    }

    private void encodePassword(UserEntity userEntity, UserData user){
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
    }
}