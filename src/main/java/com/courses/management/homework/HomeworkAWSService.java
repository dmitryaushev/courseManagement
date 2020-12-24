package com.courses.management.homework;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.courses.management.common.AWSService;
import com.courses.management.course.Course;
import com.courses.management.course.CourseRepository;
import org.apache.commons.fileupload.FileItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@Profile("aws")
public class HomeworkAWSService implements HomeworkService {

    private static final Logger LOG = LogManager.getLogger(HomeworkAWSService.class);
    private HomeworkRepository homeworkRepository;
    private CourseRepository courseRepository;
    private AWSService awsService;

    @Autowired
    public HomeworkAWSService(HomeworkRepository homeworkRepository, CourseRepository courseRepository,
                              AWSService awsService) {
        this.homeworkRepository = homeworkRepository;
        this.courseRepository = courseRepository;
        this.awsService = awsService;
    }

    @Override
    public void uploadFile(List<FileItem> items, int courseId) {
        LOG.debug(String.format("uploadFile: courseId=%d", courseId));
        Course course = courseRepository.findById(courseId).orElseThrow(() ->
                new RuntimeException(String.format("Course with id = %s not found", courseId)));
        Homework homework = null;
        try {
            for (FileItem item : items) {
                if (!item.isFormField()) {
                    homework = createHomework(course, item);
                    validateIfFileExist(homework.getTitle());
                    homeworkRepository.save(homework);
                    uploadToAWS(homework, item);
                }
            }
        } catch (Exception e) {
            LOG.error(String.format("uploadFile: courseId=%d", courseId), e);
            if (Objects.nonNull(homework) && homework.getId() != 0) {
                homeworkRepository.delete(homework);
            }
            throw new RuntimeException("Error when loading file " + e.getMessage());
        }
    }

    private Homework createHomework(Course course, FileItem item) {

        LOG.debug(String.format("createHomework: courseId=%d, fileName=%s", course.getId(), item.getName()));
        String title = new File(item.getName()).getName();
        String path = String.format("%s/%s", course.getTitle(), title);

        Homework homework = new Homework();
        homework.setCourse(course);
        homework.setTitle(title);
        homework.setPath(path);
        return homework;
    }

    private void validateIfFileExist(String title) {
        if (awsService.getS3Client().doesObjectExist(awsService.getS3BucketName(), title)) {
            throw new RuntimeException(String.format("Homework with title %s already exist", title));
        }
    }

    private void uploadToAWS(Homework homework, FileItem item) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(item.getSize());
        awsService.getS3Client().putObject(awsService.getS3BucketName(), homework.getPath(),
                item.getInputStream(), objectMetadata);
    }

    @Override
    public Homework getHomework(int id) throws IOException {
        LOG.debug(String.format("getHomework: id=%d", id));

        Homework homework = homeworkRepository.findById(id).orElseThrow(() ->
                new FileNotFoundException("File doesn't found"));

        if (!awsService.getS3Client().doesObjectExist(awsService.getS3BucketName(), homework.getPath())) {
            throw new FileNotFoundException("No file found");
        }

        S3Object s3Object = awsService.getS3Client().getObject(awsService.getS3BucketName(), homework.getPath());
        byte[] bytes = IOUtils.toByteArray(s3Object.getObjectContent());
        homework.setData(new ByteArrayInputStream(bytes));
        return homework;
    }
}
