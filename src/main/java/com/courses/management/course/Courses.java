package com.courses.management.course;

import com.courses.management.common.View;
import com.courses.management.common.commands.util.InputString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Courses {

    private static final Logger LOG = LogManager.getLogger(Courses.class);
    private CourseDAO courseDAO;
    private CourseRepository courseRepository;

    public Courses() {
    }

    public Courses(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public static Course mapCourse(InputString input) {
        String[] parameters = input.getParameters();
        String title = parameters[1];
        Course course = new Course();
        course.setTitle(title);
        course.setCourseStatus(CourseStatus.NOT_STARTED);
        return course;
    }

    public static void printCourse(View view, Course course) {
        view.write("Course:");
        view.write(String.format("\t title = %s", course.getTitle()));
        view.write(String.format("\t status = %s", course.getCourseStatus()));
        course.getUsers().forEach(x -> view.write("\t student = " + x.getFirstName() + " " + x.getLastName()));
    }

    public List<Course> showCourses() {
        return courseRepository.findAll();
    }

    public Course getById(int id) {
        return courseRepository.findById(id).orElse(new Course());
    }

    public Course getByTitle(String title) {
        return courseRepository.getByTitle(title);
    }

    public void createCourse(Course course) {
        courseRepository.save(course);
    }


}
