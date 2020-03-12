package com.courses.management.user;

import com.courses.management.common.Command;
import com.courses.management.common.View;

public class FindUserByEmail implements Command {

    private View view;
    private UserDAOImpl userDAO;

    public FindUserByEmail(View view) {
        this.view = view;
        userDAO = new UserDAOImpl();
    }

    @Override
    public String command() {
        return "find_user_by_email";
    }

    @Override
    public void process() {

        view.write("Enter email");
        String email = view.read();
        view.write(userDAO.getByEmail(email).toString());
    }
}
