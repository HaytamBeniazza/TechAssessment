package com.haytam.miniprject.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserUploadRequest {
    private MultipartFile file;
}
