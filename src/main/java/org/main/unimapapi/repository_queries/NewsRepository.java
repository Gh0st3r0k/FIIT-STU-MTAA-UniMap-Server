package org.main.unimapapi.repository_queries;

import org.main.unimapapi.dtos.News_dto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class NewsRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<News_dto> newsRowMapper = (rs, rowNum) -> {
        News_dto article = new News_dto();
        article.setId(rs.getInt("id"));
        article.setTitle(rs.getString("title"));
        article.setContent(rs.getString("content"));
        article.setDate_of_creation(rs.getString("date_of_creation"));
        return article;
    };

    public List<News_dto> findAllNews() {
        String sql = "SELECT * FROM news";
        return jdbcTemplate.query(sql, newsRowMapper);
    }
}