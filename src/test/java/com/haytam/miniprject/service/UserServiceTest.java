package com.haytam.miniprject.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haytam.miniprject.dto.request.UserGenerateRequest;
import com.haytam.miniprject.dto.response.BatchUploadResult;
import com.haytam.miniprject.dto.response.UserResponse;
import com.haytam.miniprject.entity.User;
import com.haytam.miniprject.repository.UserRepository;
import com.haytam.miniprject.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateUsers() {
        UserGenerateRequest request = new UserGenerateRequest();
        request.setCount(5);

        List<UserResponse> users = userService.generateUsers(request);
        assertEquals(5, users.size());
    }

    @Test
    void testUploadUsers() throws Exception {
        String userJson = "[{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\",\"username\":\"johndoe\",\"password\":\"password123\"}]";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "users.json",
                "application/json",
                userJson.getBytes()
        );

        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        BatchUploadResult result = userService.uploadUsers(file);
        assertEquals(1, result.getTotalRecords());
    }

    @Test
    void testGetMyProfile() {
        String usernameOrEmail = "testuser@example.com";

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(usernameOrEmail);
        SecurityContextHolder.setContext(securityContext);

        User mockUser = new User();
        mockUser.setEmail(usernameOrEmail);
        mockUser.setUsername("testuser");
        mockUser.setFirstName("Test");
        mockUser.setLastName("User");

        when(userRepository.findByEmail(usernameOrEmail)).thenReturn(Optional.of(mockUser));

        UserResponse response = userService.getMyProfile(usernameOrEmail);

        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
        assertEquals("Test", response.getFirstName());
        assertEquals("User", response.getLastName());
    }

    @Test
    void testGetUserProfile() {
        String username = "testuser";

        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setEmail("testuser@example.com");
        mockUser.setFirstName("Test");
        mockUser.setLastName("User");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        UserResponse response = userService.getUserProfile(username);

        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
        assertEquals("Test", response.getFirstName());
        assertEquals("User", response.getLastName());
    }


}