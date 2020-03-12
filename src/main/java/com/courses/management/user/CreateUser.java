package com.courses.management.user;

import com.courses.management.common.Command;
import com.courses.management.common.View;
import com.courses.management.course.CourseDAOImpl;

public class CreateUser implements Command {

    private View view;
    private UserDAOImpl userDAO;

    public CreateUser(View view) {
        this.view = view;
        userDAO = new UserDAOImpl();
    }

    @Override
    public String command() {
        return "create_user";
    }

    @Override
    public void process() {

        view.write("Enter user first name");
        String firstName = view.read();
        view.write("Enter user last name");
        String lastName = view.read();
        view.write("Enter user email");
        String email = view.read();
        view.write("Enter course title");
        String title = view.read();

        if (userDAO.getByEmail(email) == null) {

            User user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setUserRole(UserRole.NEWCOMER);
            user.setUserStatus(UserStatus.ACTIVE);
            user.setCourse(new CourseDAOImpl().get(title));

            userDAO.create(user);
            view.write("User created");
        } else
            view.write(String.format("User with email %s is already exist", email));
    }
}
