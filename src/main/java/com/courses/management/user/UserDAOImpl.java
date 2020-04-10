package com.courses.management.user;

import com.courses.management.common.exceptions.SQLUserException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Objects;

public class UserDAOImpl implements UserDAO {

    private final static Logger LOG = LogManager.getLogger(UserDAOImpl.class);
    private SessionFactory sessionFactory;

    public UserDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(User user) {

        LOG.debug(String.format("create: user.first_name=%s " +
                "user.last_name=%s" +
                "user.email=%s", user.getFirstName(), user.getLastName(), user.getEmail()));

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        } catch (Exception e) {
            transactionRollback(transaction);
            LOG.error(String.format("create: user.email=%s", user.getEmail()), e);
            throw new SQLUserException("Error occurred when creating user");
        }

    }

    @Override
    public void update(User user) {

        LOG.debug(String.format("update: user.first_name=%s " +
                "user.last_name=%s" +
                "user.email=%s", user.getFirstName(), user.getLastName(), user.getEmail()));

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(user);
            transaction.commit();
        } catch (Exception e) {
            transactionRollback(transaction);
            LOG.error(String.format("update: user.email=%s", user.getEmail()), e);
            throw new SQLUserException("Error occurred when updating user");
        }
    }

    @Override
    public void delete(User user) {

        LOG.debug(String.format("delete: user.id=%s ", user.getId()));

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.delete(user);
            transaction.commit();
        } catch (Exception e) {
            transactionRollback(transaction);
            LOG.error(String.format("delete: user.id=%s", user.getId()), e);
            throw new SQLUserException("Error occurred when removing user");
        }
    }

    @Override
    public User get(int id) {

        LOG.debug(String.format("get: user.id=%s ", id));

        try (Session session = sessionFactory.openSession()) {
            return session.get(User.class, id);
        } catch (Exception e) {
            LOG.error(String.format("get: user.id=%s", id), e);
            throw new SQLUserException("Error occurred when retrieving user");
        }
    }

    @Override
    public List<User> getAll() {

        LOG.debug("getAll: ");

        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from User", User.class).list();
        } catch (Exception e) {
            LOG.error("getAll: ", e);
            throw new SQLUserException("Error occurred when retrieving all user");
        }
    }

    @Override
    public User get(String email) {

        LOG.debug(String.format("get: user.email=%s ", email));

        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from User u where u.email=:email", User.class)
                    .setParameter("email", email).uniqueResult();
        } catch (Exception e) {
            LOG.error(String.format("get: user.email=%s", email), e);
            throw new SQLUserException("Error occurred when retrieving user");
        }
    }

    @Override
    public List<User> getUsersByCourse(String courseTitle) {

        LOG.debug(String.format("getUsersByCourse: course.title=%s", courseTitle));

        try (Session session = sessionFactory.openSession()) {
            String query = "select u " +
                    "from User u " +
                    "join u.course c " +
                    "where c.title=:courseTitle";
            return session.createQuery(query, User.class).setParameter("courseTitle", courseTitle).list();
        } catch (Exception e) {
            LOG.error(String.format("getUsersByCourse: course.title=%s", courseTitle), e);
            throw new SQLUserException("Error occurred when retrieving users by course title");
        }
    }

    @Override
    public List<User> getAllByStatus(UserStatus userStatus) {

        LOG.debug(String.format("getAllByStatus: user.status=%s", userStatus.name()));

        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from User u where u.status=:userStatus", User.class)
                    .setParameter("userStatus", userStatus).list();
        } catch (Exception e) {
            LOG.error(String.format("getAllByStatus: user.status=%s", userStatus.name()), e);
            throw new SQLUserException("Error occurred when retrieving users by status");
        }
    }

    private void transactionRollback(Transaction transaction) {
        if (Objects.nonNull(transaction)) {
            transaction.rollback();
        }
    }
}