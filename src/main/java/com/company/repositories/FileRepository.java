package com.company.repositories;

import com.company.models.FileInfo;

import java.util.Optional;

public interface FileRepository {
    void save(FileInfo fileInfo);
    Optional<String> getPath(String fileName);
    Optional<FileInfo> getByStorageName(String storageName);
}
