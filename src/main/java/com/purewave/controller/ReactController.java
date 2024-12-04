package com.purewave.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ReactController {

    @RequestMapping("/lab")
    public String forward() {
        return "forward:/index.html";
    }
}
