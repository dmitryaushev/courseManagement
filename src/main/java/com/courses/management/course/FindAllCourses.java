package com.courses.management.course;

import com.courses.management.common.Command;
import com.courses.management.common.View;

import java.util.List;

public class FindAllCourses implements Command {

    private View view;
    private CourseDAO courseDAO;

    public FindAllCourses(View view) {
        this.view = view;
        courseDAO = new CourseDAOImpl();
    }

    @Override
    public String command() {
        return "find_all_courses";
    }

    @Override
    public void process() {

        courseDAO.getAll().forEach(System.out::println);
    }
}
