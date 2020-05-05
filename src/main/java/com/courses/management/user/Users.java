package com.courses.management.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

public class Users {

    private static final Logger LOG = LogManager.getLogger(Users.class);

    private UserRepository userRepository;
    private BCryptPasswordEncoder encoder;

    public Users(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setEncoder(BCryptPasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public User getById(int id) {
        LOG.debug(String.format("getUser: id=%d", id));
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotExistException(String.format("User with id %s not found", id)));
    }

    public User getByEmail(String email) {
        LOG.debug(String.format("getUser: email=%s", email));
        return userRepository.getByEmail(email)
                .orElseThrow(() -> new UserNotExistException(String.format("User with email %s not exist", email)));
    }

    public List<User> getAll() {
        LOG.debug("getAllUsers: ");
        return userRepository.findAll();
    }

    public void registerUser(User user) {

        if (emailExist(user.getEmail())) {
            throw new UserAlreadyExistsException(
                    String.format("There is an account with that email address: %s", user.getEmail()));
        }

        user.setUserRole(UserRole.ROLE_NEWCOMER);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
    }

    private boolean emailExist(String email) {
        return userRepository.getByEmail(email).isPresent();
    }
}
