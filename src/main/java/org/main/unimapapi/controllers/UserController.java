package org.main.unimapapi.controllers;

import lombok.AllArgsConstructor;
import org.main.unimapapi.dtos.User_dto;
import org.main.unimapapi.entities.User;
import org.main.unimapapi.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/unimap_pc/user")
public class UserController {
    private final UserService userService;


    @PostMapping
    public ResponseEntity<User> create(@RequestBody User_dto dto) {
        return new ResponseEntity<>(userService.create(dto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<User>> readAll() {
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<User> update(@RequestBody User user) {
        return new ResponseEntity<>(userService.update(user), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public HttpStatus delete(@PathVariable Long id) {
        userService.delete(id);
        return HttpStatus.OK;
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getByEmail(@PathVariable String email) {
        System.out.println("A have a request with email: " + email);

        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/login/{login}")
    public ResponseEntity<User> getByLogin(@PathVariable String login) {
        System.out.println("A have a request with login: " + login);

        User user = userService.findByLogin(login);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(user);
    }
}
