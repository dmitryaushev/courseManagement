package com.courses.management.homework;

import com.courses.management.common.PropertiesUtil;
import com.courses.management.course.Course;
import com.courses.management.course.CourseDAO;
import org.apache.commons.fileupload.FileItem;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class Homeworks {

    private HomeworkDAO homeworkDAO;
    private CourseDAO courseDAO;

    public Homeworks() {
    }

    public Homeworks(HomeworkDAO homeworkDAO, CourseDAO courseDAO) {
        this.homeworkDAO = homeworkDAO;
        this.courseDAO = courseDAO;
    }

    public Homework get(int id) {
        return homeworkDAO.get(id);
    }

    public void uploadFile(List<FileItem> items, Integer courseId) {

        Course course = courseDAO.get(courseId);
        if (Objects.isNull(course)) {
            throw new RuntimeException(String.format("Course with id = %s not found", courseId));
        }

        Homework homework = null;
        try {
            for (FileItem item : items) {
                if (!item.isFormField()) {
                    homework = createHomework(course, item);
                    File file = new File(homework.getPath());
                    validateIfFileExists(file, homework.getTitle());
                    homeworkDAO.create(homework);
                    item.write(file);
                }
            }
        } catch (Exception e) {
            if (Objects.nonNull(homework) && homework.getId() != 0) {
                homeworkDAO.delete(homework);
            }
            throw new RuntimeException("Error when loading file " + e.getMessage());
        }
    }

    private void validateIfFileExists(File file, String title) {
        if (file.exists()) {
            throw new RuntimeException(String.format("Homework with title %s already exist", title));
        }
    }

    private Homework createHomework(Course course, FileItem item) {

        String title = new File(item.getName()).getName();
        String path = PropertiesUtil.getFolderPath() + File.separator + course.getTitle() + File.separator + title;

        Homework homework = new Homework();
        homework.setCourse(course);
        homework.setTitle(title);
        homework.setPath(path);
        return homework;
    }
}
