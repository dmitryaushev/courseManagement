package com.courses.management.user;

import java.util.List;

public class UserHelper {

    public static final String FIRST_USER_FIRST_NAME = "Dima";
    public static final String FIRST_USER_LAST_NAME = "Aushev";
    public static final String FIRST_USER_EMAIL = "aushev@mail.com";
    public static final String FIRST_USER_PASSWORD = "123";
    public static final String SECOND_USER_FIRST_NAME = "Anya";
    public static final String SECOND_USER_LAST_NAME = "Gorban";
    public static final String SECOND_USER_EMAIL = "gorban@mail.com";
    public static final String SECOND_USER_PASSWORD = "123";
    public static final String INCORRECT_EMAIL = "test@mail.com";


    public static User createUser(String firstName, String lastName, String email, String password) {
        User user = new User();
        user.setStatus(UserStatus.NOT_ACTIVE);
        user.setUserRole(UserRole.ROLE_NEWCOMER);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(password);
        return user;
    }

    public static List<User> prepareUsersList() {
        return List.of(UserHelper.createUser(FIRST_USER_FIRST_NAME, FIRST_USER_LAST_NAME,
                FIRST_USER_EMAIL, FIRST_USER_PASSWORD),
                UserHelper.createUser(SECOND_USER_FIRST_NAME, SECOND_USER_LAST_NAME,
                        SECOND_USER_EMAIL, SECOND_USER_PASSWORD));
    }
}
