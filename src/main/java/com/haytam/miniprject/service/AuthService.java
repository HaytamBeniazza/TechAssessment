package com.haytam.miniprject.service;

import com.haytam.miniprject.dto.request.AuthRequest;
import com.haytam.miniprject.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse authenticateUser(AuthRequest request);
}
