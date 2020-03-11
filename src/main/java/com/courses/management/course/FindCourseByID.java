package com.courses.management.course;

import com.courses.management.common.Command;
import com.courses.management.common.View;

public class FindCourseByID implements Command {

    private View view;
    private CourseDAO courseDAO;

    public FindCourseByID(View view) {
        this.view = view;
        courseDAO = new CourseDAOImpl();
    }

    @Override
    public String command() {
        return "find_course_by_id";
    }

    @Override
    public void process() {

        view.write("Enter id");
        int id = Integer.parseInt(view.read());

        view.write(courseDAO.get(id).toString());
    }
}
