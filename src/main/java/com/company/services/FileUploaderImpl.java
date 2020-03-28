package com.company.services;

import com.company.dto.FileDto;
import com.company.models.FileInfo;
import com.company.repositories.FileRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class FileUploaderImpl implements FileUploader {
    private String directory;
    private FileRepository fileRepository;
    private StorageFilenameGenerator storageFilenameGenerator;

    public FileUploaderImpl(String directory, FileRepository fileRepository, StorageFilenameGenerator storageFilenameGenerator) {
        this.directory = directory;
        this.fileRepository = fileRepository;
        this.storageFilenameGenerator = storageFilenameGenerator;
    }

    @Override
    public FileDto uploadAndSaveToDb(MultipartFile multipartFile) {
        try {
            String type = multipartFile.getContentType();
            Long size = multipartFile.getSize();
            String storageFilename = storageFilenameGenerator.generateStorageFilename();
            String path = directory + "/" + storageFilename + "/" + multipartFile.getOriginalFilename();
            multipartFile.transferTo(new File(path));
            fileRepository.save(new FileInfo(storageFilename, multipartFile.getOriginalFilename(), path, size, type));
            return new FileDto("sh.il.almaz@gmail.com", "anonymous", "localhost:8080/getfile/" + storageFilename, storageFilename, multipartFile.getOriginalFilename(), size, type);
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }

}
