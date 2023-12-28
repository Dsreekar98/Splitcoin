package com.project.Splitwise.controller;

import com.project.Splitwise.config.AuthenticationService;
import com.project.Splitwise.model.AuthenticationResponse;
import com.project.Splitwise.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody User request)
    {
        return ResponseEntity.ok(service.register(request));
    }

        @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody User request)
    {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
