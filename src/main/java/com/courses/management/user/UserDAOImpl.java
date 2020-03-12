package com.courses.management.user;

import com.courses.management.common.DataAccessObject;
import com.courses.management.common.DatabaseConnector;
import com.courses.management.course.CourseDAOImpl;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    private HikariDataSource dataSource = DatabaseConnector.getConnector();
    private final static Logger LOG = LogManager.getLogger(UserDAOImpl.class);

    private final static String GET_BY_EMAIL = "SELECT * FROM users where email = ?;";

    @Override
    public void create(User user) {

    }

    @Override
    public void update(User user) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public User get(int id) {
        return null;
    }

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public User getByEmail(String email) {

        User user = new User();

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(GET_BY_EMAIL)) {
            statement.setString(1, email);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                user.setId(resultSet.getInt("id"));
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setEmail(resultSet.getString("email"));
                user.setUserRole(UserRole.getUserRole(resultSet.getString("user_role")).get());
                user.setUserStatus(UserStatus.getUserStatus(resultSet.getString("status")).get());
                user.setCourse(new CourseDAOImpl().get(resultSet.getInt("course_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
