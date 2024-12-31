package org.main.unimapapi.dtos;

import lombok.Data;

@Data
public class User_dto {
    private String login;
    private String email;
    private String password;
    private String username;
    private boolean isAdmin;
    private boolean subscribe;
    private boolean verification;
    private int avatar;
}