package com.haytam.miniprject.service;

import com.haytam.miniprject.dto.request.AuthRequest;
import com.haytam.miniprject.dto.response.AuthResponse;
import com.haytam.miniprject.entity.User;
import com.haytam.miniprject.repository.UserRepository;
import com.haytam.miniprject.security.jwt.JwtUtil;
import com.haytam.miniprject.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticateUser() {
        AuthRequest request = new AuthRequest();
        request.setUsername("testuser");
        request.setPassword("password123");

        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmailOrUsername(any(), any())).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(any())).thenReturn("mockToken");

        AuthResponse response = authService.authenticateUser(request);
        assertNotNull(response.getAccessToken());
    }
}