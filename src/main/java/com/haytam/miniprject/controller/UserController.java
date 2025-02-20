package com.haytam.miniprject.controller;

import com.haytam.miniprject.dto.request.UserGenerateRequest;
import com.haytam.miniprject.dto.response.BatchUploadResult;
import com.haytam.miniprject.dto.response.UserResponse;
import com.haytam.miniprject.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Generate Users", security = { @SecurityRequirement(name = "bearerAuth") })
    @GetMapping("/generate")
    public ResponseEntity<List<UserResponse>> generateUsers(@RequestParam int count) {
        return ResponseEntity.ok(userService.generateUsers(new UserGenerateRequest(count)));
    }

    @PostMapping("/batch")
    public ResponseEntity<BatchUploadResult> uploadUsers(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(userService.uploadUsers(file));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();
        return ResponseEntity.ok(userService.getMyProfile(loggedInUsername));
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponse> getUserProfile(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserProfile(username));
    }
}