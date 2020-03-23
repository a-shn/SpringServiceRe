package com.company.services;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploader {
    void uploadAndSaveToDb(MultipartFile multipartFile);
}
