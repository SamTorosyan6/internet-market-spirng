package com.example.internetmarketspirng.service;

import com.example.internetmarketspirng.model.User;

import java.util.Optional;

public interface UserService {

    void save(User user);

    Optional<User> findByEmail(String username);

}
