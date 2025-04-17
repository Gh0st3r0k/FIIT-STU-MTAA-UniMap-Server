package org.main.unimapapi.controllers;

import org.main.unimapapi.services.ChangeAvatarService;
import org.main.unimapapi.utils.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/*
 * Controller for handling requests to change user avatar
 *
 * URL prefix: /api/unimap_pc
 * Method: PUT
 * Endpoint: /change_avatar
 */
@RestController
@RequestMapping("/api/unimap_pc")
public class ChangeAvatarController {

    private final ChangeAvatarService changeAvatarService;
    private final JwtToken jwtToken;

    @Autowired
    public ChangeAvatarController(ChangeAvatarService changeAvatarService, JwtToken jwtToken) {
        this.jwtToken = jwtToken;
        this.changeAvatarService = changeAvatarService;
    }

    @PutMapping(value = "/change_avatar")
    public ResponseEntity<String> changeAvatar(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody byte[] avatarData,
            @RequestParam("fileName") String fileName) {

        try {
            String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);

            if (avatarData == null || avatarData.length == 0 || decodedFileName == null || decodedFileName.isBlank()) {
           //     System.out.println("Invalid request. Avatar data and file name are required.");
                return ResponseEntity.badRequest().body("Invalid request. Avatar data and file name are required.");
            }

            String token = authorizationHeader.replace("Bearer ", "");
            if (token.isEmpty() || !jwtToken.validateAccessToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized. Token is required.");
            }

            String login = jwtToken.extractUsernameFromAccessToken(token);

            boolean avatarUpdated = changeAvatarService.updateAvatarData(login, avatarData, decodedFileName);

            if (avatarUpdated) {
           //     System.out.println("Avatar updated successfully for user: " + login);
            //    System.out.println("File name: " + decodedFileName);
            //    System.out.println("Avatar size: " + avatarData.length + " bytes");

                return ResponseEntity.ok("Avatar updated successfully.");
            } else {
                System.out.println("Failed to update avatar. User not found: " + login);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }
        } catch (Exception e) {
            System.out.println("Error processing the avatar: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the avatar file: " + e.getMessage());
        }
    }
}