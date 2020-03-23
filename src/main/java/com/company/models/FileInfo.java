package com.company.models;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder()
public class FileInfo {
    // название файла в хранилище
    private String storageFileName;
    // название файла оригинальное
    private String originalFileName;
    private String path;
    // размер файла
    private Long size;
    // тип файла (MIME)
    private String type;
}
