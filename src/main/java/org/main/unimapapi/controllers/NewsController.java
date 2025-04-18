package org.main.unimapapi.controllers;

import lombok.RequiredArgsConstructor;
import org.main.unimapapi.dtos.News_dto;
import org.main.unimapapi.utils.ServerLogger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/*
 * Controller to get the news displayed in UniMap application
 *
 * URL prefix: /api/unimap_pc/news
 * Method: GET
 * Endpoint: /all
 * Output: list of news with fields (id, title, content, date_of_creation)
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/unimap_pc/news")
public class NewsController {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<News_dto> newsRowMapper = new RowMapper<News_dto>() {
        @Override
        public News_dto mapRow(ResultSet rs, int rowNum) throws SQLException {
            News_dto article = new News_dto();
            article.setId(rs.getInt("id"));
            article.setTitle(rs.getString("title"));
            article.setContent(rs.getString("content"));
            article.setDate_of_creation(rs.getString("date_of_creation")); // Corrected line
            return article;
        }
    };

    @GetMapping("/all")
    public ResponseEntity<List<News_dto>> getAllNews() {
        try {
            String sql = "SELECT * FROM news";
            List<News_dto> newsList = jdbcTemplate.query(sql, newsRowMapper);
            return ResponseEntity.ok(newsList);
        } catch (Exception e) {
            ServerLogger.logServer(ServerLogger.Level.ERROR, "Error fetching news: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}