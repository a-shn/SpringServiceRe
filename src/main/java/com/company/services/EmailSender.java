package com.company.services;

public interface EmailSender {
    void sendEmail(String subject, String email, String emailContent);
}
