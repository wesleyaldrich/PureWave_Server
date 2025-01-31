package com.purewave.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/token")
    public ResponseEntity<Map<String, String>> getToken(@CookieValue(name = "authToken", required = false) String authToken) {
        if (authToken != null) {
            return ResponseEntity.ok(Collections.singletonMap("token", authToken));
        }
        return ResponseEntity.ok(Collections.singletonMap("token", null));
    }
}
