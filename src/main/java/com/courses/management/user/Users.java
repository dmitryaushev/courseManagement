package com.courses.management.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Users {

    private static final Logger LOG = LogManager.getLogger(Users.class);
    private static String EMAIL_REGEXP = "^[-a-z0-9!#$%&'*+/=?^_`{|}~]+(?:\\.[-a-z0-9!#$%&'*+/=?^_`{|}~]+)*@(?:[a-z0-9](" +
            "[-a-z0-9]{0,61}[a-z0-9])?\\.)*(?:aero|arpa|asia|biz|cat|com|coop|edu|gov|info|int|jobs|mil|mobi|museum|name|" +
            "net|org|pro|tel|travel|[a-z][a-z])$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEXP);

    private UserRepository userRepository;

    public Users(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static void validateEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (!matcher.matches()) {
            LOG.warn(String.format("validateEmail: email %s is incorrect", email));
            throw new IllegalArgumentException(String.format("Wrong email address %s", email));
        }
    }

    public User getById(int id) {
        return userRepository.findById(id).orElse(new User());
    }

    public User getByEmail(String email) {
        return userRepository.getByEmail(email);
    }
}
