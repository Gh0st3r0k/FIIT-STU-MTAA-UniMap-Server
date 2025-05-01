package org.main.unimapapi.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LogEntry {
    private final int userId;
    private final String timestamp;
    private final String level;
    private final String message;
}