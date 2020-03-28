package com.company.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class FileDto {
    private String email;
    private String login;
    private String url;
    private String storageFileName;
    private String originalFileName;
    private Long size;
    private String type;
}
