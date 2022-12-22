package com.arguvos.transcriber.api;

import com.arguvos.transcriber.service.HealthcheckService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.arguvos.transcriber.config.AppConstant.*;

@Slf4j
@Controller
@RequestMapping(value = HEALTHCHECK_PAGE)
public class HealthcheckEndpoint {

    private final HealthcheckService healthcheckService;

    @Autowired
    public HealthcheckEndpoint(HealthcheckService healthcheckService) {
        this.healthcheckService = healthcheckService;
    }

    @GetMapping
    public String healthcheck(Model model) {
        model.addAttribute(HEALTHCHECK_ATTRIBUTE, healthcheckService.healthcheck());
        return HEALTHCHECK_PAGE;
    }
}
