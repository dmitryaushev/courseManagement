package com.courses.management.user;

import com.courses.management.course.CourseDAOImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    private DataSource dataSource;
    private final static Logger LOG = LogManager.getLogger(UserDAOImpl.class);

    private final static String INSERT = "INSERT INTO users(first_name, last_name, email, user_role, status, course_id) " +
            "VALUES (?, ?, ?, ?, ?, ?);";

    private final static String GET_BY_EMAIL = "SELECT * FROM users where email = ?;";

    public UserDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void create(User user) {

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(INSERT)) {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getUserRole().getRole());
            statement.setString(5, user.getStatus().getStatus());
            statement.setInt(6, user.getCourse().getId());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

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
    public User get(String email) {

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
                user.setStatus(UserStatus.getUserStatus(resultSet.getString("status")).get());
                user.setCourse(new CourseDAOImpl(dataSource).get(resultSet.getInt("course_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
