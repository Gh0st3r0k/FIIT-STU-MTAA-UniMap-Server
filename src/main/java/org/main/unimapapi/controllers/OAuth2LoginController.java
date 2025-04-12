package org.main.unimapapi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/*
 * Controller for OAuth2 login processing (Google and Facebook)
 *
 * URL prefix: /api/unimap_pc/authenticate
 */
@Controller
public class OAuth2LoginController {

    /*
     * Method: GET
     * Endpoint: /api/unimap_pc/authenticate/google
     */
    @GetMapping("/api/unimap_pc/authenticate/google")
    public String googleLogin() {
        return "redirect:/oauth2/authorization/google";
    }

    /*
     * Method: GET
     * Endpoint: /api/unimap_pc/authenticate/facebook
     */
    @GetMapping("/api/unimap_pc/authenticate/facebook")
    public String facebookLogin() {
        return "redirect:/oauth2/authorization/facebook";
    }

    /*
     * Method: GET
     * Endpoint: /login-success
     * Response: plain text about successful login
     */
    @GetMapping("/login-success")
    @ResponseBody
    public String loginSuccess() {
        return "Successful login!\n You can back to the application.";
    }
}