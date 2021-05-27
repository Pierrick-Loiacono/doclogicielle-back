package com.groupe3.api.controller;

import com.groupe3.api.model.User;
import com.groupe3.api.model.Usertype;
import com.groupe3.api.schema.request.LoginRequest;
import com.groupe3.api.schema.request.SubscriptionRequest;
import com.groupe3.api.schema.response.LoginResponse;
import com.groupe3.api.service.UserService;
import com.groupe3.api.utils.Security;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Controller that contains endpoints relative to users and authentication
 * @author romainchahine
 * @see User
 * @see RestController
 */
@RestController
public class UserController {

    private final UserService us;
    private final Security security;

    /**
     * Constructor
     * @param us autowired UserService
     * @param sec autowired Security class
     * @see Autowired
     */
    @Autowired
    public UserController(UserService us, Security sec) {
        this.us = us;
        this.security = sec;
    }

    /**
     * Endpoint for login
     * @param login request body
     * @see Security
     * @return a JWT
     */
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest login) {
        System.out.println(login.getMail());
        LoginResponse response = new LoginResponse();
        Optional<User> opt = this.us.retrieveUser(login.getMail());
        if(opt.isPresent()) {
            try {
                User user = opt.get();
                if(Security.checkPassword(login.getPassword(), user.getPassword())) {
                    HashMap<String, Object> payload = new HashMap<>();

                    payload.put("id", user.getId());
                    payload.put("mail", user.getMail());

                    String token = this.security.generateJWT(payload);
                    response.setToken(token);

                } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");

            } catch (JoseException ex) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "User not found"
            );
        }
        return response;
    }

    @GetMapping("/user/me")
    public User me(@RequestHeader("Authorization") String token) {
        try {
            final Map<String, Object> payload = this.security.readJWT(token);

            Optional<User> opt = this.us.getById((Integer)payload.get("id"));

            if(opt.isPresent()) {

                return opt.get();

            } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        } catch (InvalidJwtException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
}
