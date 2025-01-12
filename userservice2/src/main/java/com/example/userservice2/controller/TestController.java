package com.example.userservice2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/ms1")
    public String getMethodName(@RequestParam(value = "param", required = false) String param) {
        return "Hello from ms2: " + param;
    }
    
}
