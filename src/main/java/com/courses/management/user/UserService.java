package com.courses.management.user;

import java.util.List;

public interface UserService {

    User getUser(int id);
    User getUser(String email);
    List<User> getAllUsers();
    void registerUser(User user);
}
