package com.courses.management.user;

import com.courses.management.common.Command;
import com.courses.management.common.View;
import com.courses.management.common.commands.util.Commands;
import com.courses.management.common.commands.util.InputString;

import java.util.Objects;

public class FindUser implements Command {

    private View view;
    private UserDAO userDAO;

    public FindUser(View view, UserDAO userDAO) {
        this.view = view;
        this.userDAO = userDAO;
    }

    @Override
    public String command() {
        return Commands.FIND_USER;
    }

    @Override
    public void process(InputString inputString) {

        String email = inputString.getParameters()[1];
        User user = userDAO.get(email);
        if (Objects.isNull(user)) {
            throw new IllegalArgumentException("Can't find user with provided email");
        }
        Users.printUser(view, user);
    }
}
