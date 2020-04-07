package com.courses.management.course;

import com.courses.management.common.exceptions.SQLCourseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CourseDAOImpl implements CourseDAO {

    private final static Logger LOG = LogManager.getLogger(CourseDAOImpl.class);
    private DataSource dataSource;
    private SessionFactory sessionFactory;

    private final static String INSERT = "INSERT INTO course(title, status) VALUES(?, ?);";
    private final static String GET_BY_ID = "SELECT id, title, status FROM course WHERE id = ?;";
    private final static String GET_BY_TITLE = "SELECT id, title, status FROM course WHERE title = ?;";
    private static final String GET_ALL = "SELECT id, title, status FROM course;";
    private static final String GET_ALL_BY_STATUS = "SELECT id, title, status FROM course WHERE status = ?;";
    private final static String UPDATE = "UPDATE course SET title = ?, status = ? WHERE id = ?;";

    public CourseDAOImpl(DataSource dataSource, SessionFactory sessionFactory) {
        this.dataSource = dataSource;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Course course)  {

        Session session = null;
        Transaction transaction;

        LOG.debug(String.format("create: course.title=%s", course.getTitle()));
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(course);
            transaction.commit();
        } catch (Exception e) {
            LOG.error(String.format("create: course.title=%s", course.getTitle()), e);
            throw new SQLCourseException("Error occurred when saving a course");
        } finally {
            if (Objects.nonNull(session)) {
                session.close();
            }
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
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Course c where  c.id=:id", Course.class)
                    .setParameter("id", id).uniqueResult();
        } catch (Exception e) {
            LOG.error(String.format("get: course.id = %d", id), e);
            throw new SQLCourseException("Error occurred when find a course");
        }
    }

    @Override
    public Course get(String title) {

        LOG.debug(String.format("find: course.title = %s", title));
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Course c where c.title=:title", Course.class)
                    .setParameter("title", title).uniqueResult();
        } catch (Exception e) {
            LOG.error(String.format("get: course.title=%s", title), e);
            throw new SQLCourseException("Error occurred when find a course");
        }
    }

    @Override
    public List<Course> getAll() {

        LOG.debug("getAll");
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Course", Course.class).list();
        } catch (Exception e) {
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
