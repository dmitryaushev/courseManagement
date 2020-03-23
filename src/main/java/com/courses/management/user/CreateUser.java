package com.courses.management.user;

import com.courses.management.common.Command;
import com.courses.management.common.View;
import com.courses.management.common.commands.util.Commands;
import com.courses.management.common.commands.util.InputString;

import java.util.Objects;

public class CreateUser implements Command {

    private View view;
    private UserDAO userDAO;

    public CreateUser(View view, UserDAO userDAO) {
        this.view = view;
        this.userDAO = userDAO;
    }

    @Override
    public String command() {
        return Commands.CREATE_USER;
    }

    @Override
    public void process(InputString inputString) {
        User user = Users.mapUser(inputString);
        validateUser(user);
        userDAO.create(user);
        view.write(String.format("User %s %s created!", user.getFirstName(), user.getLastName()));
    }

    private void validateUser(User user) {
        if (user.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("User first name can't be empty");
        }
        if (user.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("User last name can't be empty");
        }
        if (Objects.nonNull(userDAO.get(user.getEmail()))) {
            throw new IllegalArgumentException(String.format("User with email %s already exists", user.getEmail()));
        }
    }
}
