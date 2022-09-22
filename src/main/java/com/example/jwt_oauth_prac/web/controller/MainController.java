package com.example.jwt_oauth_prac.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/success")
    public String success() {
        return "success";
    }

    @GetMapping("/fail")
    public String fail() {
        return "fail";
    }

    @GetMapping("/guest")
    public String guest() {
        return "guest";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }
}
