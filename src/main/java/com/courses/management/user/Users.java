package com.courses.management.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Users {

    private static final Logger LOG = LogManager.getLogger(Users.class);

    private UserRepository userRepository;

    public Users(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotExistError(String.format("User with id %s not found", id)));
    }

    public User getByEmail(String email) {
        return userRepository.getByEmail(email)
                .orElseThrow(() -> new UserNotExistError(String.format("User with email %s not exist", email)));
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }
}
