package com.courses.management.course;

import com.courses.management.common.Command;
import com.courses.management.common.View;
import com.courses.management.common.commands.util.Commands;
import com.courses.management.common.commands.util.InputString;

import java.util.List;

public class ShowCoursesByStatus implements Command {

    private View view;
    private CourseDAO courseDAO;
    private static final int COURSE_STATUS_INDEX = 1;

    public ShowCoursesByStatus(View view, CourseDAO courseDAO) {
        this.view = view;
        this.courseDAO = courseDAO;
    }

    @Override
    public String command() {
        return Commands.SHOW_COURSES_BY_STATUS;
    }

    @Override
    public void process(InputString input) {

        String status = input.getParameters()[COURSE_STATUS_INDEX];
        CourseStatus courseStatus = CourseStatus.getCourseStatus(status).orElseThrow(() ->
                new IllegalArgumentException("Course status incorrect"));

        List<Course> courses = courseDAO.getAllByStatus(courseStatus);
        if (courses.isEmpty()) {
            view.write("There is no courses by specified status");
        } else {
            courses.forEach(x -> Courses.printCourse(view, x));
        }
    }
}
