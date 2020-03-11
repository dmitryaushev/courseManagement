package com.courses.management.course;

import com.courses.management.common.DataAccessObject;
import com.courses.management.common.DatabaseConnector;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CourseDAOImpl implements CourseDAO {

    private HikariDataSource dataSource = DatabaseConnector.getConnector();
    private final static Logger LOG = LogManager.getLogger(CourseDAOImpl.class);

    private final static String INSERT = "INSERT INTO course(title, status) " +
            "VALUES(?, ?);";
    private final static String GET_BY_ID = "SELECT * FROM course WHERE id = ?;";
    private final static String GET_BY_TITLE = "SELECT * FROM course WHERE title = ?;";


    @Override
    public void create(Course course) {
        LOG.debug(String.format("create: course.title=%s", course.getTitle()));

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT)) {
            statement.setString(1, course.getTitle());
            statement.setString(2, course.getCourseStatus().getStatus());
            statement.execute();
        } catch (SQLException e) {
            LOG.error(String.format("create: course.title=%s", course.getTitle()), e);
        }
    }

    @Override
    public void update(Course course) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public Course get(int id) {
        LOG.debug(String.format("find: course.id = %d", id));

        Course course = new Course();

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(GET_BY_ID)) {
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                course.setId(id);
                course.setTitle(resultSet.getString("title"));
                String s = resultSet.getString("status");
                course.setCourseStatus(CourseStatus.getCourseStatus(s).get());
            } else
                LOG.debug(String.format("wrong input: course.id = %d", id));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return course;
    }

    @Override
    public List<Course> getAll() {
        return null;
    }

    @Override
    public Course get(String title) {
        LOG.debug(String.format("find: course.title = %s", title));

        Course course = new Course();

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(GET_BY_TITLE)) {
            statement.setString(1, title);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                course.setId(resultSet.getInt("id"));
                course.setTitle(title);
                String s = resultSet.getString("status");
                course.setCourseStatus(CourseStatus.getCourseStatus(s).get());
            } else
                LOG.debug(String.format("wrong input: course.title = %s", title));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return course;
    }
}
