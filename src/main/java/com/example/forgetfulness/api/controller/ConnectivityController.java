package com.example.forgetfulness.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ConnectivityController {
    @GetMapping("/connect/test")
    public ResponseEntity<String> connectivityTest() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
