package com.courses.management.user;

import com.courses.management.common.Command;
import com.courses.management.common.View;
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
        return "create_user|firstName|lastName|email";
    }

    @Override
    public void process(InputString inputString) {
        User user = Users.mapUser(inputString);
        validateEmailExist(user.getEmail());
        userDAO.create(user);
        view.write("User created");
    }

    private void validateEmailExist(String email) {
        if (Objects.nonNull(userDAO.get(email))) {
            throw new IllegalArgumentException(String.format("User with email %s already exists", email));
        }
    }
}
