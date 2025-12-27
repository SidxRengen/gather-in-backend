package com.example.getherinjava.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/healthCheck")
public class HealthCheck {
    @GetMapping
    public ResponseEntity<?> getAppHealth(){
        Map<String,String> response = new HashMap<>();
        response.put("message","Gather in App is running well");
        response.put("status","UP");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
