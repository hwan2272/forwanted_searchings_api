package com.hwan2272.forwanted.searchingsapi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
public class TestController {


    @GetMapping("/hello")
    public String hello() {
        return "welcome";
    }


}
