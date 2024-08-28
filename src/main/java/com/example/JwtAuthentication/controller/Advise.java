package com.example.JwtAuthentication.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/advise")
public class Advise {
    @GetMapping("/get")
    public ResponseEntity<String> getAdvise() {
        return ResponseEntity.ok("Don't worry, be happy!");
    }
}
