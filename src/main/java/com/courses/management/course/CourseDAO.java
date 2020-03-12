package com.courses.management.course;

import com.courses.management.common.DataAccessObject;

import java.util.List;

public interface CourseDAO extends DataAccessObject<Course> {
    Course get(String title);
    void delete(String title);
    List<Course> getAllByStatus(String status);
}
