package com.hwan2272.forwanted.searchingsapi.controller;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TestControllerTest {

    @Test
    @Order(1)
    public void hello() {
        System.out.println("welcome");
    }
}