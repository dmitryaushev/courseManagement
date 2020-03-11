package com.courses.management.course;

import com.courses.management.common.Command;
import com.courses.management.common.View;

public class FindCourseByTitle implements Command {

    private View view;
    private CourseDAO courseDAO;

    public FindCourseByTitle(View view) {
        this.view = view;
        courseDAO = new CourseDAOImpl();
    }

    @Override
    public String command() {
        return "find_course_by_title";
    }

    @Override
    public void process() {

        view.write("Enter title");
        String title = view.read();

        view.write(courseDAO.get(title).toString());
    }
}
