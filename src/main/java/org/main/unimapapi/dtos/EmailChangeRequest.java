package org.main.unimapapi.dtos;

import lombok.Data;

@Data
public class EmailChangeRequest {
    private String login;
    private String email;
}
