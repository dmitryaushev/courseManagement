package com.courses.management.user;

public class UsersTest {
    public static User getTestUser(){
        User user = new User();
        user.setFirstName("Dmitry");
        user.setLastName("Aushev");
        user.setEmail("dmitryaushev@gmail.com");
        user.setUserRole(UserRole.ROLE_NEWCOMER);
        user.setStatus(UserStatus.NOT_ACTIVE);
        return user;
    }
}
