package org.main.unimapapi.controllers;

import lombok.RequiredArgsConstructor;
import org.main.unimapapi.dtos.News_dto;
import org.main.unimapapi.repository_queries.NewsRepository;
import org.main.unimapapi.utils.ServerLogger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
    private final NewsRepository newsRepository;

    @GetMapping("/all")
    public ResponseEntity<List<News_dto>> getAllNews() {
        try {
            List<News_dto> newsList = newsRepository.findAllNews();
      //      System.out.println("Fetching news: " + newsList);

            return ResponseEntity.ok(newsList);
        } catch (Exception e) {
           // System.out.println("Error fetching news: " + e.getMessage());
            ServerLogger.logServer(ServerLogger.Level.ERROR, "Error fetching news: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}