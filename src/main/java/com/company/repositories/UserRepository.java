package com.company.repositories;

import com.company.models.User;

import java.util.Optional;

public interface UserRepository {
    void save(User user);

    Optional<User> findByEmail(String email);
}
