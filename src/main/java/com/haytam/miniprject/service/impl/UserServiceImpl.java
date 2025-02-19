package com.haytam.miniprject.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haytam.miniprject.dto.request.UserGenerateRequest;
import com.haytam.miniprject.dto.response.BatchUploadResult;
import com.haytam.miniprject.dto.response.UserResponse;
import com.haytam.miniprject.entity.User;
import com.haytam.miniprject.repository.UserRepository;
import com.haytam.miniprject.service.UserService;
import com.haytam.miniprject.util.PasswordEncoder;
import com.haytam.miniprject.util.UserDataGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    @Override
    public List<UserResponse> generateUsers(UserGenerateRequest request) {
        List<User> users = UserDataGenerator.generateUsers(request.getCount());
        return users.stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BatchUploadResult uploadUsers(MultipartFile file) {
        try {
            List<User> users = objectMapper.readValue(file.getInputStream(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, User.class));

            int totalRecords = users.size();
            int successfulImports = 0;
            int failedImports = 0;

            for (User user : users) {
                if (!userRepository.existsByEmail(user.getEmail()) && !userRepository.existsByUsername(user.getUsername())) {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    userRepository.save(user);
                    successfulImports++;
                } else {
                    failedImports++;
                }
            }

            return new BatchUploadResult(totalRecords, successfulImports, failedImports);
        } catch (IOException e) {
            throw new RuntimeException("Failed to process file", e);
        }
    }

    @Override
    public UserResponse getMyProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToUserResponse(user);
    }

    @Override
    public UserResponse getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToUserResponse(user);
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthDate(user.getBirthDate())
                .city(user.getCity())
                .country(user.getCountry())
                .avatar(user.getAvatar())
                .company(user.getCompany())
                .jobPosition(user.getJobPosition())
                .mobile(user.getMobile())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}