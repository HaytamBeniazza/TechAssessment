package com.haytam.miniprject.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haytam.miniprject.dto.request.UserGenerateRequest;
import com.haytam.miniprject.dto.response.BatchUploadResult;
import com.haytam.miniprject.dto.response.UserResponse;
import com.haytam.miniprject.entity.User;
import com.haytam.miniprject.exception.FileProcessingException;
import com.haytam.miniprject.exception.UserNotFoundException;
import com.haytam.miniprject.repository.UserRepository;
import com.haytam.miniprject.service.UserService;
import com.haytam.miniprject.util.UserDataGenerator;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.haytam.miniprject.exception.DuplicateUserException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
    }


    @Override
    public List<UserResponse> generateUsers(UserGenerateRequest request) {
        List<User> users = UserDataGenerator.generateUsers(request.getCount());

        for (User user : users) {
            if (userRepository.existsByEmail(user.getEmail()) || userRepository.existsByUsername(user.getUsername())) {
                throw new DuplicateUserException("User with email or username already exists: " + user.getUsername());
            }
        }

        userRepository.saveAll(users);
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
            throw new FileProcessingException("Failed to process uploaded file", e);
        }
    }

    @Override
    public UserResponse getMyProfile(String usernameOrEmail) {
        User user = userRepository.findByEmail(usernameOrEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + usernameOrEmail));
        return mapToUserResponse(user);
    }

    @Override
    public UserResponse getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
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