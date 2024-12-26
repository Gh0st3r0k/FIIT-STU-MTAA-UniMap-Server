package org.main.unimapapi.controllers;

import org.main.unimapapi.dtos.ServerConnection;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/unimap_pc")

public class ConnectionController {
    @GetMapping("/check-connection")
    public ResponseEntity<ServerConnection> checkConnection() {
        ServerConnection status = new ServerConnection(
                "UP",
                LocalDateTime.now().toString()
        );
        System.out.println("Response sent: " + status);
        return ResponseEntity.ok(status);
    }
}