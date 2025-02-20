package com.haytam.miniprject.controller;

import com.haytam.miniprject.dto.response.BatchUploadResult;
import com.haytam.miniprject.dto.response.UserResponse;
import com.haytam.miniprject.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @TestConfiguration
    static class MockUserServiceConfiguration {
        @Bean
        public UserService userService() {
            return Mockito.mock(UserService.class);
        }
    }


    @Test
    void testGenerateUsers() throws Exception {
        when(userService.generateUsers(any())).thenReturn(Collections.singletonList(new UserResponse()));

        mockMvc.perform(get("/api/users/generate?count=5"))
                .andExpect(status().isOk());
    }

    @Test
    void testUploadUsers() throws Exception {
        String userJson = "[]";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "users.json",
                "application/json",
                userJson.getBytes()
        );

        when(userService.uploadUsers(any())).thenReturn(new BatchUploadResult(0, 0, 0));

        mockMvc.perform(multipart("/api/users/batch")
                        .file(file)
                        .contentType("multipart/form-data"))
                .andExpect(status().isOk());
    }
}