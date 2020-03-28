package com.company.aspects;

import com.company.dto.FileDto;
import com.company.services.EmailSender;
import com.company.services.TemplateResolver;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Aspect
public class FileLinkSenderAspect {
    @Autowired
    private TemplateResolver templateResolver;
    @Autowired
    private EmailSender emailSender;

    @AfterReturning(pointcut = "execution(* com.company.services.FileUploader.uploadAndSaveToDb(..))",
            returning = "resultDto")
    public void logAfterReturning(JoinPoint joinPoint, FileDto resultDto) {
        Map<String, String> root = new HashMap<>();
        root.put("login", resultDto.getLogin());
        root.put("url", resultDto.getUrl());
        root.put("originalFileName", resultDto.getOriginalFileName());
        String html = templateResolver.process("file_link_email.ftlh", root);
        emailSender.sendEmail("File link.", resultDto.getEmail(), html);
    }
}
