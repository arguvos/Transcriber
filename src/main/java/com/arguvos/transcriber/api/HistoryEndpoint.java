package com.arguvos.transcriber.api;

import com.arguvos.transcriber.service.HistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

import static com.arguvos.transcriber.config.AppConstant.*;

@Slf4j
@Controller
@RequestMapping(value = HISTORY_PAGE)
public class HistoryEndpoint {

    private final HistoryService historyService;

    @Autowired
    public HistoryEndpoint(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping
    public String history(Principal principal, Model model) {
        model.addAttribute("history", historyService.getHistoryByUser(principal.getName()));
        return HISTORY_PAGE;
    }
}
