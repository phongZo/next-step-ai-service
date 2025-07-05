package com.nextsteprecommendengine.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
public class FileService {
    static final String[] UPLOAD_TYPES = new String[]{"LOGO", "AVATAR", "IMAGE", "DOCUMENT", "CV"};
    static final String[] ALLOWED_EXTENSIONS = new String[]{"pdf", "doc", "docx"};

    @Value("${file.upload-dir}")
    private String uploadDir;

    public Resource loadFileAsResource(String relativePath) {
        try {
            String clean = relativePath;
            while (clean.startsWith("/") || clean.startsWith("\\")) {
                clean = clean.substring(1);
            }
            Path filePath = Paths
                    .get(uploadDir, clean)
                    .toAbsolutePath()
                    .normalize();

            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            }
            log.error("File not found or unreadable: {}", filePath);
        } catch (MalformedURLException e) {
            log.error("URL: {}", relativePath, e);
        }
        return null;
    }
}
