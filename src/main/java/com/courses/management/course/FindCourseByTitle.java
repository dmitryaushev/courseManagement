package com.courses.management.course;

import com.courses.management.common.Command;
import com.courses.management.common.View;
import com.courses.management.common.commands.util.InputString;

public class FindCourseByTitle implements Command {

    private View view;
    private CourseDAO courseDAO;

    public FindCourseByTitle(View view) {
        this.view = view;
        courseDAO = new CourseDAOImpl();
    }

    @Override
    public String command() {
        return "find_course|title";
    }

    @Override
    public void process(InputString input) {

        input.validateParameters(command());
        String title = input.getParameters()[1];
        Course course = courseDAO.get(title);
        if (course == null) {
            throw new IllegalArgumentException("Can't find course with provided title");
        }
        Courses.printCourse(view, course);
    }
}
