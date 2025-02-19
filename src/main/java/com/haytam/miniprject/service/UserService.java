package com.haytam.miniprject.service;

import com.haytam.miniprject.dto.request.UserGenerateRequest;
import com.haytam.miniprject.dto.response.BatchUploadResult;
import com.haytam.miniprject.dto.response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    List<UserResponse> generateUsers(UserGenerateRequest request);
    BatchUploadResult uploadUsers(MultipartFile file);
    UserResponse getMyProfile(String username);
    UserResponse getUserProfile(String username);
}
