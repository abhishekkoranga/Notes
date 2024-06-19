package com.speer.assessmet.notes.controller;

import com.speer.assessmet.notes.dto.LoginDto;
import com.speer.assessmet.notes.dto.SignUpDto;
import com.speer.assessmet.notes.service.AuthnService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
@Slf4j
@RateLimiter(name = "rtr")
public class Authn {

    @Autowired
    AuthnService service;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpDto dto) {
        try {
            service.signup(dto);
        } catch (Exception e) {
            return ResponseEntity.
                    badRequest().
                    body(e.getLocalizedMessage());
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto) {
        String userDetailsEncoded = null;
        try {
            userDetailsEncoded = service.login(dto);
        } catch (Exception e) {
            return ResponseEntity.
                    badRequest().
                    body(e.getLocalizedMessage());
        }

        HttpHeaders authHeader = new HttpHeaders();
        authHeader.add("Authorization", userDetailsEncoded);
        return ResponseEntity.
                ok().
                headers(authHeader).
                build();

    }
}
