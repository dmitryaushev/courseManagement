package com.courses.management.course;

import com.courses.management.common.Command;
import com.courses.management.common.View;

import java.util.Arrays;

public class FindCoursesByStatus implements Command {

    private View view;
    private CourseDAOImpl courseDAO;

    public FindCoursesByStatus(View view) {
        this.view = view;
        courseDAO = new CourseDAOImpl();
    }

    @Override
    public String command() {
        return "find_courses_by_status";
    }

    @Override
    public void process() {

        view.write("Choose a course status");
        Arrays.stream(CourseStatus.values()).forEach(System.out::println);
        String status = view.read();
        courseDAO.getAllByStatus(status).forEach(System.out::println);
    }
}
