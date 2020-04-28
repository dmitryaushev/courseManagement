package com.courses.management.course;

public class CourseAlreadyExistError extends RuntimeException {
    public CourseAlreadyExistError(String message) {
        super(message);
    }
}
