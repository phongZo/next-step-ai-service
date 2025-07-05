package com.nextsteprecommendengine.api.service.feign;

import com.nextsteprecommendengine.api.dto.AIProcessResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

@FeignClient(name = "ai-service", url = "http://0.0.0.0:8000")
public interface AIFeign {
    @PostMapping(value = "/upload_and_process", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    AIProcessResponseDto uploadAndProcess(@RequestPart("file") MultipartFile file);
}
