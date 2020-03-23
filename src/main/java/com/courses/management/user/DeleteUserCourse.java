package com.courses.management.user;

import com.courses.management.common.Command;
import com.courses.management.common.View;
import com.courses.management.common.commands.util.Commands;
import com.courses.management.common.commands.util.InputString;

import java.util.Objects;

public class DeleteUserCourse implements Command {

    private View view;
    private UserDAO userDAO;
    private static final int EMAIL_INDEX = 1;

    public DeleteUserCourse(View view, UserDAO userDAO) {
        this.view = view;
        this.userDAO = userDAO;
    }

    @Override
    public String command() {
        return Commands.DELETE_USER_COURSE;
    }

    @Override
    public void process(InputString input) {

        String[] parameters = input.getParameters();
        String email = parameters[EMAIL_INDEX];
        User user = userDAO.get(email);
        if (Objects.isNull(user)) {
            throw new IllegalArgumentException("Can't find user with provided email");
        }

        user.setCourse(null);
        user.setStatus(UserStatus.NOT_ACTIVE);
        userDAO.removeUserCourseAndSetStatus(email, UserStatus.NOT_ACTIVE);
        view.write(String.format("User course removed and status set to %s", UserStatus.NOT_ACTIVE.getStatus()));
    }
}
