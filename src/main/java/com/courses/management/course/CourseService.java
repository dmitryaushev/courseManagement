package com.courses.management.course;

import java.util.List;

public interface CourseService {

    List<Course> showCourses();
    Course getById(int id);
    Course getByTitle(String title);
    void createCourse(Course course);

}
