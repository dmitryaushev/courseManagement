package com.courses.management.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select u from User u where u.email=?1")
    User getByEmail(String email);
}
