package com.courses.management.course;

import com.courses.management.common.exceptions.SQLCourseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Objects;

public class CourseDAOImpl implements CourseDAO {

    private final static Logger LOG = LogManager.getLogger(CourseDAOImpl.class);
    private SessionFactory sessionFactory;

    public CourseDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Course course)  {

        LOG.debug(String.format("create: course.title=%s", course.getTitle()));

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(course);
            transaction.commit();
        } catch (Exception e) {
            if (Objects.nonNull(transaction)) {
                transaction.rollback();
            }
            LOG.error(String.format("create: course.title=%s", course.getTitle()), e);
            throw new SQLCourseException("Error occurred when saving a course");
        }
    }

    @Override
    public void update(Course course) {

        LOG.debug(String.format("update: course.title=%s, course.status=%s",
                course.getTitle(), course.getCourseStatus()));

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(course);
            transaction.commit();
        } catch (Exception e) {
            if (Objects.nonNull(transaction)) {
                transaction.rollback();
            }
            LOG.error(String.format("update: course.title=%s", course.getTitle()), e);
            throw new SQLCourseException("Error occurred when update a course");
        }
    }

    @Override
    public void delete(Course course) {
        throw new UnsupportedOperationException("Course can't be deleted");
    }

    @Override
    public Course get(int id) {

        LOG.debug(String.format("find: course.id = %d", id));

        try (Session session = sessionFactory.openSession()) {
            return session.get(Course.class, id);
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

        LOG.debug("getAll: ");

        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Course", Course.class).list();
        } catch (Exception e) {
            LOG.error("getAll", e);
            throw new SQLCourseException("Error occurred when get all courses");
        }
    }

    @Override
    public List<Course> getAllByStatus(CourseStatus courseStatus) {

        LOG.debug(String.format("getAllByStatus: course.status=%s", courseStatus.name()));

        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Course c where c.courseStatus=:courseStatus", Course.class)
                    .setParameter("courseStatus", courseStatus).list();
        } catch (Exception e) {
            LOG.error(String.format("getAllByStatus: user.status=%s", courseStatus.name()), e);
            throw new SQLCourseException("Error occurred when get all courses by status");
        }
    }
}