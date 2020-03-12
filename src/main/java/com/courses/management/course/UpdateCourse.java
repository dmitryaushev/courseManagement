package com.courses.management.course;

import com.courses.management.common.Command;
import com.courses.management.common.View;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateCourse implements Command {

    private View view;
    private CourseDAO courseDAO;

    public UpdateCourse(View view) {
        this.view = view;
        courseDAO = new CourseDAOImpl();
    }

    @Override
    public String command() {
        return "update_course";
    }

    @Override
    public void process() {

        List<Course> coursesList = courseDAO.getAll();

        view.write("Enter course id");
        int id = Integer.parseInt(view.read());

        view.write("Enter new course title");
        String title = view.read();

        for (Course course : coursesList)
            if (course.getTitle().equals(title)) {
                view.write("This title is already exist");
                return;
            }

        List<String> courseStatusList = Arrays.stream(CourseStatus.values())
                .map(CourseStatus::getStatus)
                .collect(Collectors.toList());

        view.write("Choose a course status");
        courseStatusList.forEach(System.out::println);
        String status = view.read();

        Course course = new Course();
        course.setId(id);
        course.setTitle(title);
        course.setCourseStatus(CourseStatus.getCourseStatus(status).get());

        courseDAO.update(course);
        view.write("Course updated");
    }
}
