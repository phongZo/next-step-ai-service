package com.nextsteprecommendengine.api.dto;

import lombok.Data;
import org.apache.tomcat.jni.FileInfo;

@Data
public class AIProcessResponseDto {
    private String status;
    private String message;
    private Object data;
    private FileInfo fileInfo;
    private String parserUsed;
    private boolean success;
} 