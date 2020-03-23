package com.company.services;

import com.company.models.User;
import com.company.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class SignUpServiceImpl implements SignUpService {
    SignUpPool signUpPool;
    UserRepository userRepository;
    EmailSender emailSender;
    @Autowired
    TemplateResolver templateResolver;

    public SignUpServiceImpl(SignUpPool signUpPool, UserRepository userRepository, EmailSender emailSender) {
        this.signUpPool = signUpPool;
        this.userRepository = userRepository;
        this.emailSender = emailSender;
    }

    @Override
    public boolean signUp(String login, String email, String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (userRepository.findByEmail(email).isEmpty()) {
            User user = new User(login, email, encoder.encode(password));
            String link = createLink(user);
            signUpPool.put(user, link, LocalDateTime.now());
            Map<String, String> map = new HashMap<>();
            map.put("login", login);
            map.put("link", link);
            String html = templateResolver.process("verification_email_message.ftlh", map);
            emailSender.sendEmail("Email verification.", user.getEmail(), html);
            return true;
        } else {
            return false;
        }
    }

    private String createLink(User user) {
        try {
            String tmp = user.getEmail() + LocalDateTime.now().toString();
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(tmp.getBytes());
            byte[] digest = md.digest();
            String hash = Base64.getEncoder().encodeToString(digest);;
            String part1 = "http://localhost:8080/springservice/verification";
            String part2 = "?hash=" + hash;
            return part1 + part2;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException();
        }
    }
}
