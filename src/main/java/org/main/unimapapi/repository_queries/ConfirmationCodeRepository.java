package org.main.unimapapi.repository_queries;

import org.main.unimapapi.entities.ConfirmationCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/*
 * Repository for working with confirmation codes
 *
 * Used for email confirmation and password recovery
 * Table: confirm_codes
 */
@Repository
public class ConfirmationCodeRepository {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    // RowMapper to convert the result of the SQL query into a ConfirmationCode object
    private final RowMapper<ConfirmationCode> confirmationCodeRowMapper = new RowMapper<ConfirmationCode>() {
        @Override
        public ConfirmationCode mapRow(ResultSet rs, int rowNum) throws SQLException {
            ConfirmationCode code = new ConfirmationCode();
            code.setUserId(rs.getLong("id_code"));
            code.setCode(rs.getString("code"));
            code.setExpirationTime(rs.getTimestamp("exp_time").toLocalDateTime());
            return code;
        }
    };

    public ConfirmationCodeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Checks for a confirmation code by userId and the code itself
    public boolean find(Long userId, String code) {
        String sql = "SELECT * FROM confirm_codes WHERE id_code = ? and code = ?";
        List<ConfirmationCode> results = jdbcTemplate.query(sql, confirmationCodeRowMapper, userId, code);
        System.out.println(results);
        return !results.isEmpty();
    }

    // Saves the new confirmation code to the database
    public void save(ConfirmationCode confirmationCode) {
        // The only way injection may appear here is
        // When the code is null, jdbc may not screen it properly
        if (confirmationCode.getCode() == null) {
            throw new IllegalArgumentException("CODE CAN NOT BE NULL");
        }

        // If it is ok - continuing
        String sql = "INSERT INTO confirm_codes (id_code, code, exp_time) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, confirmationCode.getUserId(), confirmationCode.getCode(), confirmationCode.getExpirationTime());
    }

    // Removes the confirmation code by userId and code
    public void deleteByUserId(Long userId, String code) {
        String sql = "DELETE FROM confirm_codes WHERE code = ? and id_code = ?";
        jdbcTemplate.update(sql, code, userId);
    }

}