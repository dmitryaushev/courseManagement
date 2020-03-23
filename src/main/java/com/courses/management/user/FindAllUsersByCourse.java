package com.courses.management.user;

import com.courses.management.common.Command;
import com.courses.management.common.View;
import com.courses.management.common.commands.util.Commands;
import com.courses.management.common.commands.util.InputString;

import java.util.List;

public class FindAllUsersByCourse implements Command {

    private View view;
    private UserDAO userDAO;
    private static final int COURSE_TITLE_INDEX = 1;

    public FindAllUsersByCourse(View view, UserDAO userDAO) {
        this.view = view;
        this.userDAO = userDAO;
    }

    @Override
    public String command() {
        return Commands.FIND_ALL_USERS_BY_COURSE;
    }

    @Override
    public void process(InputString input) {

        String[] parameters = input.getParameters();
        String courseTitle = parameters[COURSE_TITLE_INDEX];
        List<User> users = userDAO.getUsersByCourse(courseTitle);
        if (users.isEmpty()) {
            view.write("There is no users by specified course title");
        } else {
            users.forEach(user -> Users.printUser(view, user));
        }
    }
}