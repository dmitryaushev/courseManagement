package com.courses.management.course;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class Courses {

    private static final Logger LOG = LogManager.getLogger(Courses.class);
    private CourseRepository courseRepository;

    @Autowired
    public Courses(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> showCourses() {
        LOG.debug("showCourses: ");
        return courseRepository.findAll();
    }

    public Course getById(int id) {
        LOG.debug(String.format("getById: id=%d", id));
        return courseRepository.findById(id).orElse(new Course());
    }

    public Course getByTitle(String title) {
        LOG.debug(String.format("getByTitle: title=%s", title));
        return courseRepository.getByTitle(title);
    }

    public void createCourse(Course course) {
        LOG.debug(String.format("createCourse: courseTitle=%s", course.getTitle()));
        if (Objects.nonNull(getByTitle(course.getTitle()))){
            throw new CourseAlreadyExistError(String.format("course with title %s already exists", course.getTitle()));
        }
        courseRepository.save(course);
    }


}
