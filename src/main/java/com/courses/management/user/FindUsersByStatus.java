package com.courses.management.user;

import com.courses.management.common.Command;
import com.courses.management.common.View;
import com.courses.management.common.commands.util.Commands;
import com.courses.management.common.commands.util.InputString;

import java.util.List;

public class FindUsersByStatus implements Command {

    private View view;
    private UserDAO userDAO;
    private static final int STATUS_INDEX = 1;

    public FindUsersByStatus(View view, UserDAO userDAO) {
        this.view = view;
        this.userDAO = userDAO;
    }

    @Override
    public String command() {
        return Commands.FIND_ALL_USERS_BY_STATUS;
    }

    @Override
    public void process(InputString input) {

        String[] parameters = input.getParameters();
        String inputStatus = parameters[STATUS_INDEX];
        UserStatus userStatus = UserStatus.getUserStatus(inputStatus).orElseThrow(() ->
                new IllegalArgumentException("User status incorrect"));
        List<User> users = userDAO.getAllByStatus(userStatus);
        if (users.isEmpty()) {
            view.write("There is no users by specified status");
        } else {
            users.forEach(user -> Users.printUser(view, user));
        }
    }
}