package com.courses.management.homework;

import com.courses.management.common.exceptions.SQLHomeworkException;
import com.courses.management.course.CourseDAOImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Objects;

public class HomeworkDAOImpl implements HomeworkDAO {

    private final static Logger LOG = LogManager.getLogger(CourseDAOImpl.class);
    private SessionFactory sessionFactory;

    public HomeworkDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Homework homework) {

        LOG.debug(String.format("create: homework.title=%s homework.path=%s homework.course_id=%s",
                homework.getTitle(), homework.getPath(), homework.getCourse().getId()));

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(homework);
            transaction.commit();
        } catch (Exception e) {
            transactionRollback(transaction);
            LOG.error(String.format("create: homework.title=%s", homework.getTitle()), e);
            throw new SQLHomeworkException("Error occurred when creating a homework");
        }

    }

    @Override
    public void update(Homework homework) {

        LOG.debug(String.format("update: homework.title=%s homework.path=%s homework.course_id=%s",
                homework.getTitle(), homework.getPath(), homework.getCourse().getId()));

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            session.update(homework);
            transaction.commit();
        } catch (Exception e) {
            transactionRollback(transaction);
            LOG.error(String.format("update: homework.title=%s", homework.getTitle()), e);
            throw new SQLHomeworkException("Error occurred when updating a homework");
        }
    }

    @Override
    public void delete(Homework homework) {

        LOG.debug(String.format("delete: homework.id=%s ", homework.getId()));

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.delete(homework);
            transaction.commit();
        } catch (Exception e) {
            transactionRollback(transaction);
            LOG.error(String.format("delete: homework.id=%s ", homework.getId()), e);
            throw new SQLHomeworkException("Error occurred when removing homework");
        }
    }

    @Override
    public Homework get(int id) {

        LOG.debug(String.format("get: homework.id=%s ", id));

        try (Session session = sessionFactory.openSession()){
            return session.get(Homework.class, id);
        } catch (Exception e) {
            LOG.error(String.format("get: homework.id=%s", id), e);
            throw new SQLHomeworkException("Error occurred when retrieving a homework");
        }
    }

    @Override
    public List<Homework> getAll() {

        LOG.debug("getAll: ");

        try (Session session = sessionFactory.openSession()){
            return session.createQuery("from Homework", Homework.class).list();
        } catch (Exception e) {
            LOG.error("getAll: ", e);
            throw new SQLHomeworkException("Error occurred when retrieving all homeworks");
        }
    }

    @Override
    public List<Homework> getAll(int courseId) {
        LOG.debug(String.format("getAll by courseId=%s: ", courseId));

        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Homework where course.id=:courseId", Homework.class)
                    .setParameter("courseId", courseId).list();
        } catch (Exception e) {
            LOG.error(String.format("getAll by courseId=%s: ", courseId), e);
            throw new SQLHomeworkException("Error occurred when retrieving all homeworks by courseId");
        }
    }

    private void transactionRollback(Transaction transaction) {
        if (Objects.nonNull(transaction)) {
            transaction.rollback();
        }
    }
}
