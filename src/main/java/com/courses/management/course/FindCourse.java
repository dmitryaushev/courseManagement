package com.courses.management.course;

import com.courses.management.common.Command;
import com.courses.management.common.View;
import com.courses.management.common.commands.util.Commands;
import com.courses.management.common.commands.util.InputString;

import java.util.Objects;

public class FindCourse implements Command {

    private View view;
    private CourseDAO courseDAO;

    public FindCourse(View view) {
        this.view = view;
        courseDAO = new CourseDAOImpl();
    }

    @Override
    public String command() {
        return Commands.FIND_COURSE;
    }

    @Override
    public void process(InputString input) {

        String title = input.getParameters()[1];
        Course course = courseDAO.get(title);
        if (Objects.isNull(course)) {
            throw new IllegalArgumentException("Can't find course with provided title");
        }
        Courses.printCourse(view, course);
    }
}
