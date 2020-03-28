package com.company.services;

import com.company.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploader {
    FileDto uploadAndSaveToDb(MultipartFile multipartFile);
}
