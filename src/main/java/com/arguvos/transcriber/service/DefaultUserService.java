package com.arguvos.transcriber.service;


import com.arguvos.transcriber.exception.UserAlreadyExistException;
import com.arguvos.transcriber.model.Role;
import com.arguvos.transcriber.model.UserEntity;
import com.arguvos.transcriber.repository.UserRepository;
import com.arguvos.transcriber.service.model.UserData;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service("userService")
public class DefaultUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

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


    public boolean checkIfUserExist(String email, String username) {
        return userRepository.findByEmail(email) != null || userRepository.findByUsername(username) != null;
    }

    private void encodePassword( UserEntity userEntity, UserData user){
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
    }
}