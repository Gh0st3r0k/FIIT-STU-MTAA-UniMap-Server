package org.main.unimapapi.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.main.unimapapi.dtos.User_dto;
import org.main.unimapapi.entities.ConfirmationCode;
import org.main.unimapapi.entities.User;
import org.main.unimapapi.services.*;
import org.main.unimapapi.utils.EmailSender;
import org.main.unimapapi.utils.Hashing;
import org.main.unimapapi.utils.JwtToken;
import org.main.unimapapi.utils.ServerLogger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseCookie;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/unimap_pc/")
public class UserController {
    private final UserService userService;
    private final RegistrationService registrationService;
    private final AuthService authService;
    private final ConfirmationCodeService confirmationCodeService;
    private final JwtToken jwtToken;

    @PostMapping("register")
    public ResponseEntity<User> register(@RequestBody String jsonData) {
        try {
            System.out.println("TEST "+jsonData);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonData);

            String data = jsonNode.get("data").asText();
            String[] parts = data.split(":");
            if (parts.length != 4) {

                System.out.println("TEST2");

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            String username = parts[0];
            String password = parts[2];
            String passwordHash = Hashing.hashPassword(password);
            String login = parts[1];
	    String email = parts[3];


            System.out.println("TEST3");
            if (userService.findByLogin(login).isPresent() ||
                    userService.findByEmail(email).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            User user = registrationService.register(new User_dto(login, email, passwordHash, username, false, false,null));

            System.out.println("TEST4 "+user.getLogin()+" "+user.getEmail()+" "+user.getPassword()+" "+user.getUsername());

            if (user == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            //ServerLogger.logServer(ServerLogger.Level.INFO, "User registered successfully: login=" + login);
            return ResponseEntity.ok(user);
        } catch (Exception e) {

            e.printStackTrace(); // Log the exception details

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("authenticate")
    public ResponseEntity<?> authenticate(@RequestBody String jsonData) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonData);
            String data = jsonNode.get("data").asText();
            String[] parts = data.split(":");
            if (parts.length != 2) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            String login = parts[0];
            String password = parts[1];

            User user = authService.authenticate(login, password);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String accessToken = jwtToken.generateAccessToken(user.getLogin());
            String refreshToken = jwtToken.generateRefreshToken(user.getLogin());

            ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(86400) // 1 day
                    .build();

            user.setPassword(null);
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                    .body(Map.of(
                            "user", user,
                            "accessToken", accessToken
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("user/email/{email}")
    public ResponseEntity<Void> confirmEmailExists(@PathVariable String email) {
        try {
            Optional<User> user = userService.findByEmail(email);
            if (user.isPresent()) {
                String confirmationCode = confirmationCodeService.generateRandomCode();
                LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(1);
                ConfirmationCode confirmationCodeEntity = new ConfirmationCode(user.get().getId(), confirmationCode, expirationTime);
                confirmationCodeService.save(confirmationCodeEntity);
                EmailSender.sendEmail(email, confirmationCode);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("user/email/password")
    public ResponseEntity<Void> changePassword(@RequestBody String jsonData) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonData);
            String data = jsonNode.get("data").asText();
            String[] parts = data.split(":");

            System.out.println("TEST "+data);
            if (parts.length != 2) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            String email = parts[0];
            String new_password = parts[1];

            if (userService.findByEmail(email).isPresent()) {
                User user = userService.findByEmail(email).get();
                user.setPassword(Hashing.hashPassword(new_password));
                userService.update(user);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("user/email/code")
    public ResponseEntity<Boolean> compareCodes(@RequestBody String jsonData) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonData);
            String data = jsonNode.get("data").asText();
            String[] parts = data.split(":");
            if (parts.length != 2) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            String email = parts[0];
            String userCode = parts[1];

            System.out.println("TEST "+data);

            Optional<User> user = userService.findByEmail(email);
            Long id = user.map(User::getId).orElse(null);

            System.out.println("TEST2 "+id +" "+userCode);

            boolean isCodeValid = confirmationCodeService.validateConfirmationCode(id, userCode);
            System.out.println("TEST3 "+isCodeValid);
            return ResponseEntity.ok(isCodeValid);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
