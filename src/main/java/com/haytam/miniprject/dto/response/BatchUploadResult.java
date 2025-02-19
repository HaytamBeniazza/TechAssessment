package com.haytam.miniprject.dto.response;

import lombok.Data;

@Data
public class BatchUploadResult {
    private int totalRecords;
    private int successfulImports;
    private int failedImports;

    public BatchUploadResult(int totalRecords, int successfulImports, int failedImports) {
        this.totalRecords = totalRecords;
        this.successfulImports = successfulImports;
        this.failedImports = failedImports;
    }
}
