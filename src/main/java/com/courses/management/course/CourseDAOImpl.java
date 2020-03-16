package com.courses.management.course;

import com.courses.management.common.DatabaseConnector;
import com.courses.management.common.exceptions.SQLCourseException;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDAOImpl implements CourseDAO {

    private HikariDataSource dataSource = DatabaseConnector.getConnector();
    private final static Logger LOG = LogManager.getLogger(CourseDAOImpl.class);

    private final static String INSERT = "INSERT INTO course(title, status) VALUES(?, ?);";
    private final static String GET_BY_ID = "SELECT id, title, status FROM course WHERE id = ?;";
    private final static String GET_BY_TITLE = "SELECT id, title, status FROM course WHERE title = ?;";
    private static final String GET_ALL = "SELECT id, title, status FROM course;";
    private static final String GET_ALL_BY_STATUS = "SELECT id, title, status FROM course WHERE status = ?;";
    private final static String UPDATE = "UPDATE course SET title = ?, status = ? WHERE id = ?;";

    @Override
    public void create(Course course)  {

        LOG.debug(String.format("create: course.title=%s", course.getTitle()));
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(INSERT)) {
            statement.setString(1, course.getTitle());
            statement.setString(2, course.getCourseStatus().getStatus());
            statement.execute();
        } catch (SQLException e) {
            LOG.error(String.format("create: course.title=%s", course.getTitle()), e);
            throw new SQLCourseException("Error occurred when saving a course");
        }
    }

    @Override
    public void update(Course course) {

        LOG.debug(String.format("update: course.title=%s, course.status=%s",
                course.getTitle(), course.getCourseStatus()));
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(UPDATE)) {
            statement.setString(1, course.getTitle());
            statement.setString(2, course.getCourseStatus().getStatus());
            statement.setInt(3, course.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOG.error(String.format("update: course.title=%s", course.getTitle()), e);
            throw new SQLCourseException("Error occurred when update a course");
        }
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException("Course can't be deleted");
    }

    @Override
    public Course get(int id) {

        LOG.debug(String.format("find: course.id = %d", id));
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(GET_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            return getCourse(resultSet);
        } catch (SQLException e) {
            LOG.error(String.format("get: course.id = %d", id), e);
            throw new SQLCourseException("Error occurred when find a course");
        }
    }

    @Override
    public Course get(String title) {

        LOG.debug(String.format("find: course.title = %s", title));
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(GET_BY_TITLE)) {
            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();
            return getCourse(resultSet);
        } catch (SQLException e) {
            LOG.error(String.format("get: course.title=%s", title), e);
            throw new SQLCourseException("Error occurred when find a course");
        }
    }

    @Override
    public List<Course> getAll() {

        LOG.debug("getAll");
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(GET_ALL)) {
            ResultSet resultSet = statement.executeQuery();
            return getCourseList(resultSet);
        } catch (SQLException e) {
            LOG.error("getAll", e);
            throw new SQLCourseException("Error occurred when get all courses");
        }
    }

    @Override
    public List<Course> getAllByStatus(String status) {

        List<Course> coursesList = new ArrayList<>();

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(GET_ALL_BY_STATUS)) {
            statement.setString(1, status);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                Course course = new Course();
                course.setId(resultSet.getInt("id"));
                course.setTitle(resultSet.getString("title"));
                course.setCourseStatus(CourseStatus.getCourseStatus(resultSet.getString("status")).get());
                coursesList.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return coursesList;
    }

    private Course getCourse(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return mapCourseFromRS(resultSet);
        }
        return null;
    }

    private List<Course> getCourseList(ResultSet resultSet) throws SQLException {
        List<Course> courses = new ArrayList<>();
        while (resultSet.next()) {
            courses.add(mapCourseFromRS(resultSet));
        }
        return courses;
    }

    private Course mapCourseFromRS(ResultSet resultSet) throws SQLException {

        Course course = new Course();
        course.setId(resultSet.getInt("id"));
        course.setTitle(resultSet.getString("title"));
        course.setCourseStatus(CourseStatus.getCourseStatus(resultSet.getString("status")).get());
        return course;
    }
}
