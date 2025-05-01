package org.main.unimapapi.repository_queries;

import org.main.unimapapi.dtos.Comment_dto;
import org.main.unimapapi.services.TokenService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CommentsRepository {

    private final JdbcTemplate jdbcTemplate;
    private final TokenService tokenService;

    private final RowMapper<Comment_dto> commentsRowMapper = (rs, rowNum) -> {
        Comment_dto comment = new Comment_dto();
        comment.setUser_id(rs.getInt("user_id"));
        comment.setComment_id(rs.getInt("comment_id"));
        comment.setName(rs.getString("name"));
        comment.setDescription(rs.getString("description"));
        comment.setRating(rs.getString("rating"));
        comment.setLevelAccess(rs.getInt("levelaccess"));

        if (hasColumn(rs, "subject_code")) {
            comment.setLooking_id(rs.getString("subject_code"));
        } else if (hasColumn(rs, "teacher_id")) {
            comment.setLooking_id(rs.getString("teacher_id"));
        }

        return comment;
    };

    public List<Comment_dto> getAllSubjectsComments(String subjectId) {
        String sql = "SELECT u_d.name, c_s.* FROM comments_subjects c_s INNER JOIN user_data u_d ON c_s.user_id = u_d.id WHERE c_s.subject_code = ?";
        return jdbcTemplate.query(sql, new Object[]{subjectId}, commentsRowMapper);
    }

    public List<Comment_dto> getAllTeachersComments(String teacherId) {
        String sql = "SELECT u_d.name, c_t.* FROM comments_teachers c_t INNER JOIN user_data u_d ON c_t.user_id = u_d.id WHERE c_t.teacher_id = ?";
        return jdbcTemplate.query(sql, new Object[]{teacherId}, commentsRowMapper);
    }

    public void addNewSubjectComment(int userId, String subjectCode, String description, int rating, int levelAccess) {
        String sql = "INSERT INTO comments_subjects (user_id, subject_code, description, rating, levelaccess) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, userId, subjectCode, description, rating, levelAccess);
    }

    public void addNewTeacherComment(int userId, String teacherId, String description, int rating, int levelAccess) {
        String sql = "INSERT INTO comments_teachers (user_id, teacher_id, description, rating, levelaccess) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, userId, teacherId, description, rating, levelAccess);
    }

    public void deleteSubjectComment(int commentId) {
        String sql = "DELETE FROM comments_subjects WHERE comment_id = ?";
        jdbcTemplate.update(sql, commentId);
    }

    public void deleteTeacherComment(int commentId) {
        String sql = "DELETE FROM comments_teachers WHERE comment_id = ?";
        jdbcTemplate.update(sql, commentId);
    }

    private boolean hasColumn(ResultSet rs, String columnName) {
        try {
            rs.findColumn(columnName);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}