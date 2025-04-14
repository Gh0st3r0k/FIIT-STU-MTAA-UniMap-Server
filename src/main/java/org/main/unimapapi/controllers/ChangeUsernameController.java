package org.main.unimapapi.controllers;

import org.main.unimapapi.dtos.AvatarChangeRequest;
import org.main.unimapapi.dtos.UsernameChangeRequest;
import org.main.unimapapi.services.ChangeUsernameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
 * Controller for handling username change requests
 *
 * URL prefix: /api/unimap_pc
 * Method: POST
 * Endpoint: /change_username
 */
@RestController
@RequestMapping("/api/unimap_pc")
public class ChangeUsernameController {

    private final ChangeUsernameService changeUsernameService;

    @Autowired
    public ChangeUsernameController(ChangeUsernameService changeUsernameService) {
        this.changeUsernameService = changeUsernameService;
    }

    @PutMapping("/change_username")
    public ResponseEntity<String> changeAvatar(@RequestBody UsernameChangeRequest request) {
        if (request == null || request.getEmail() == null || request.getUsername() == null) {
            return ResponseEntity.badRequest().body("Invalid request. Email and avatar path are required.");
        }

        boolean avatarChanged = changeUsernameService.changeUsername(request.getEmail(), request.getUsername());

        if (avatarChanged) {
            return ResponseEntity.ok("Avatar changed successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }
}