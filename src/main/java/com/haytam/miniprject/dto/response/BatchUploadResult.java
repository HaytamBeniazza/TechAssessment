package com.haytam.miniprject.dto.response;

import lombok.Data;

@Data
public class BatchUploadResult {
    private int totalRecords;
    private int successfulImports;
    private int failedImports;
}
