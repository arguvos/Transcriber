package com.arguvos.transcriber.api;

import com.arguvos.transcriber.exception.UserAlreadyExistException;
import com.arguvos.transcriber.service.DefaultUserService;
import com.arguvos.transcriber.service.model.UserData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

import static com.arguvos.transcriber.config.AppConstant.*;

@Slf4j
@Controller
@RequestMapping(value = REGISTER_PAGE)
public class RegistrationEndpoint {

    @Autowired
    private DefaultUserService userService;

    @GetMapping()
    public String register(final Model model) {
        model.addAttribute("userData", new UserData());
        return REGISTER_PAGE;
    }

    @PostMapping()
    public String userRegistration(final @Valid UserData userData, final BindingResult bindingResult, final Model model) {
        log.info("Registration new user {}", userData.getEmail());
        if (bindingResult.hasErrors()) {
            model.addAttribute("registrationForm", userData);
            return REGISTER_PAGE;
        }
        try {
            userService.register(userData);
        } catch (UserAlreadyExistException e) {
            bindingResult.rejectValue("email", "userData.email", "An account already exists for this email.");
            model.addAttribute("registrationForm", userData);
            return "register";
        }
        return INDEX_PAGE;
    }
}
