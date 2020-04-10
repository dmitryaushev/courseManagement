package com.courses.management.user;

import com.courses.management.common.exceptions.SQLUserException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserDAOImpl implements UserDAO {

    private final static Logger LOG = LogManager.getLogger(UserDAOImpl.class);
    private DataSource dataSource;
    private SessionFactory sessionFactory;

    private static final String GET_USERS_BY_COURSE_TITLE = "SELECT u.id, u.first_name, u.last_name, u.email, u.user_role, u.status " +
            "FROM users u " +
            "INNER JOIN course c ON c.id=u.course_id " +
            "WHERE c.title=?;";
    private static final String GET_ALL_USERS_BY_STATUS = "SELECT id, first_name, last_name, email, user_role, status " +
            "FROM users WHERE status=?;";

    public UserDAOImpl(DataSource dataSource, SessionFactory sessionFactory) {
        this.dataSource = dataSource;
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
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(GET_USERS_BY_COURSE_TITLE)) {
            statement.setString(1, courseTitle);
            ResultSet resultSet = statement.executeQuery();
            return getUserList(resultSet);
        } catch (SQLException e) {
            LOG.error(String.format("getUsersByCourse: course.title=%s", courseTitle), e);
            throw new SQLUserException("Error occurred when retrieving users by course title");
        }
    }

    @Override
    public List<User> getAllByStatus(UserStatus userStatus) {

        LOG.debug(String.format("getAllByStatus: user.status=%s", userStatus.name()));
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(GET_ALL_USERS_BY_STATUS)) {
            statement.setString(1, userStatus.name());
            return getUserList(statement.executeQuery());
        } catch (SQLException e) {
            LOG.error(String.format("getAllByStatus: user.status=%s", userStatus.name()), e);
            throw new SQLUserException("Error occurred when retrieving users by status");
        }
    }

    private User getUser(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return mapUserFromRS(resultSet);
        }
        return null;
    }

    private List<User> getUserList(ResultSet rs) throws SQLException {
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            users.add(mapUserFromRS(rs));
        }
        return users;
    }

    private User mapUserFromRS(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setEmail(resultSet.getString("email"));
        user.setUserRole(UserRole.getUserRole(resultSet.getString("user_role")).get());
        user.setStatus(UserStatus.getUserStatus(resultSet.getString("status")).get());
        return user;
    }

    private void transactionRollback(Transaction transaction) {
        if (Objects.nonNull(transaction)) {
            transaction.rollback();
        }
    }

}
