package com.company.services;

import com.company.repositories.FileRepository;

public class StorageFilenameGeneratorImpl implements StorageFilenameGenerator {
    FileRepository fileRepository;

    public StorageFilenameGeneratorImpl() {
    }

    public String generateStorageFilename() {
        return "2";
    }
}
