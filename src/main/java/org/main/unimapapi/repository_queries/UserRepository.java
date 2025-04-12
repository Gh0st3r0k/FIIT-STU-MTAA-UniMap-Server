package org.main.unimapapi.repository_queries;

import org.main.unimapapi.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/*
 * Repository for working with user table (`user_data`)
 *
 * Used in:
 * - AuthService / RegistrationService / UserService
 * - UserController → registration, login, data change, deletion
 */
@Repository
public class UserRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Converts the SQL query result string into a User object
    private final RowMapper<User> userRowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setLogin(rs.getString("login"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setUsername(rs.getString("name"));
            user.setAdmin(rs.getBoolean("is_admin"));
            user.setPremium(rs.getBoolean("is_premium"));
            user.setAvatar(rs.getString("avatar_path"));
            return user;
        }
    };

    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM user_data WHERE id = ?";
        List<User> users = jdbcTemplate.query(sql, userRowMapper, id);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    public Optional<User> findByEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("EMAIL CAN NOT BE NULL");
        }

        String sql = "SELECT * FROM user_data WHERE email = ?";
        List<User> users = jdbcTemplate.query(sql, userRowMapper, email);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    public Optional<User> findByLogin(String login) {
        if (login == null) {
            throw new IllegalArgumentException("LOGIN CAN NOT BE NULL");
        }

        String sql = "SELECT * FROM user_data WHERE login = ?";
        List<User> users = jdbcTemplate.query(sql, userRowMapper, login);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    public Optional<User> findByUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("EMAIL CAN NOT BE NULL");
        }

        String sql = "SELECT * FROM user_data WHERE name = ?";
        List<User> users = jdbcTemplate.query(sql, userRowMapper, username);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM user_data";
        return jdbcTemplate.query(sql, userRowMapper);
    }

    // Saving a new user
    public void save(User user) {
        String sql = "INSERT INTO user_data (login, email, password, name, is_admin,is_premium,avatar_path) VALUES (?, ?, ?, ?,?, ?, ?)";
        jdbcTemplate.update(sql, user.getLogin(), user.getEmail(), user.getPassword(), user.getUsername(), user.isAdmin(),user.isPremium(), user.getAvatar());
    }

    // Updating an existing user by ID
    public void update(User user) {
        String sql = "UPDATE user_data SET login = ?, email = ?, password = ?, name = ?, is_admin = ?, is_premium= ?,avatar_path = ? WHERE id = ?";
        jdbcTemplate.update(sql, user.getLogin(), user.getEmail(), user.getPassword(), user.getUsername(), user.isAdmin(),user.isPremium(),user.getAvatar(), user.getId());
    }

    // Deleting a user by ID
    public void deleteById(Long id) {
        String sql = "DELETE FROM user_data WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    /*
     * Deletes EVERYTHING related to the user:
     * - comments on subjects and teachers
     * - confirmation codes
     * - the account itself
     */
    public void deleteAllUserInfo(Long id) {
        String deleteCommentsSubjectsSql = "DELETE FROM comments_subjects WHERE user_id = ?";
        String deleteCommentsTeachersSql = "DELETE FROM comments_teachers WHERE user_id = ?";
        String deleteUserSql = "DELETE FROM user_data WHERE id = ?";
        String deleteConfCodesSql = "DELETE FROM confirm_codes WHERE id_code = ?";

        jdbcTemplate.update(deleteCommentsSubjectsSql, id);
        jdbcTemplate.update(deleteCommentsTeachersSql, id);
        jdbcTemplate.update(deleteConfCodesSql, id);
        jdbcTemplate.update(deleteUserSql, id);

    }

    // Deletes only the user's comments
    public void deleteAllUserComments(Long id) {
        String deleteCommentsSubjectsSql = "DELETE FROM comments_subjects WHERE user_id = ?";
        String deleteCommentsTeachersSql = "DELETE FROM comments_teachers WHERE user_id = ?";

        jdbcTemplate.update(deleteCommentsSubjectsSql, id);
        jdbcTemplate.update(deleteCommentsTeachersSql, id);
    }








}