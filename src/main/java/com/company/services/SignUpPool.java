package com.company.services;

import com.company.models.User;
import com.company.repositories.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class SignUpPool {
    private Map<String, User> signUpPool;
    private Map<String, LocalDateTime> linkCreationTimes;
    private Map<String, String> linksOfEmails;
    private UserRepository userRepository;

    public SignUpPool(UserRepository userRepository) {
        signUpPool = new HashMap<>();
        linkCreationTimes = new HashMap<>();
        linksOfEmails = new HashMap<>();
        this.userRepository = userRepository;
    }

    public void put(User user, String link, LocalDateTime time) {
        deleteLinks(user.getEmail());
        signUpPool.put(link, user);
        linkCreationTimes.put(link, time);
        linksOfEmails.put(user.getEmail(), link);
    }

    public boolean verify(String link) {
        try {
            if (ChronoUnit.HOURS.between(linkCreationTimes.get(link), LocalDateTime.now()) > 24) {
                deleteLinks(signUpPool.get(link).getEmail());
                return false;
            } else {
                userRepository.save(signUpPool.get(link));
                deleteLinks(signUpPool.get(link).getEmail());
                return true;
            }
        } catch (NullPointerException e) {
            return false;
        }
    }

    private void deleteLinks(String email) {
        if (linksOfEmails.containsKey(email)) {
            signUpPool.remove(linksOfEmails.get(email));
            linkCreationTimes.remove(linksOfEmails.get(email));
            linksOfEmails.remove(email);
        }
    }
}
