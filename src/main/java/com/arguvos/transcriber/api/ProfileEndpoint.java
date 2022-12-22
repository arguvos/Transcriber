package com.arguvos.transcriber.api;

import com.arguvos.transcriber.service.UserService;
import com.arguvos.transcriber.service.model.UserData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Validation;
import javax.validation.Validator;

import java.security.Principal;

import static com.arguvos.transcriber.config.AppConstant.*;

@Slf4j
@Controller
@RequestMapping(value = PROFILE_PAGE)
public class ProfileEndpoint {

    private final UserService userService;
    private final Validator validator;

    public ProfileEndpoint(UserService userService) {
        this.userService = userService;
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @GetMapping()
    public String profile(Principal principal, Model model) {
        model.addAttribute(USER_ATTRIBUTE, userService.findByUsername(principal.getName()));
        return PROFILE_PAGE;
    }

    @PostMapping()
    public String update(UserData userData, Principal principal, Model model) {
        if (userData.getEmail() != null && validator.validate(userData.getEmail()).isEmpty()) {
            userService.updateEmail(principal.getName(), userData.getEmail());
        }
        if (userData.getPassword() != null && validator.validate(userData.getPassword()).isEmpty()) {
            userService.updatePassword(principal.getName(), userData.getPassword());
        }
        model.addAttribute(USER_ATTRIBUTE, userService.findByUsername(principal.getName()));
        return PROFILE_PAGE;
    }
}
