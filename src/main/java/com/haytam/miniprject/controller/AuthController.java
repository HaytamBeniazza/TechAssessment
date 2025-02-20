package com.haytam.miniprject.controller;

import com.haytam.miniprject.dto.request.AuthRequest;
import com.haytam.miniprject.dto.response.AuthResponse;
import com.haytam.miniprject.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Authenticate User")
    @PostMapping
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.authenticateUser(request));
    }
}