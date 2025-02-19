package com.haytam.miniprject.service.impl;

import com.haytam.miniprject.dto.request.AuthRequest;
import com.haytam.miniprject.dto.response.AuthResponse;
import com.haytam.miniprject.entity.User;
import com.haytam.miniprject.repository.UserRepository;
import com.haytam.miniprject.security.jwt.JwtUtil;
import com.haytam.miniprject.service.AuthService;
import com.haytam.miniprject.util.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponse authenticateUser(AuthRequest request) {
        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        // Fetch user details
        User user = userRepository.findByEmailOrUsername(request.getUsername(), request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Generate JWT
        String accessToken = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(accessToken);
    }
}