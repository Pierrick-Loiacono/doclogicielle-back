package com.groupe3.api.controller;

import com.groupe3.api.model.User;
import com.groupe3.api.model.Usertype;
import com.groupe3.api.schema.request.LoginRequest;
import com.groupe3.api.schema.response.LoginResponse;
import com.groupe3.api.service.UserService;
import com.groupe3.api.utils.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Controller that contains endpoints relative to users and authentication
 * @author Groupe3
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
    @PostMapping("/authentication/login")
    public LoginResponse login(@RequestBody LoginRequest login) {
        LoginResponse response = new LoginResponse();
        Optional<User> opt = this.us.retrieveUser(login.getMail());
        if(opt.isPresent()) {
            User user = opt.get();
            if(Security.checkPassword(login.getPassword(), user.getPassword())) {

                response.setId(user.getId()); // On n'utilise plus le JWT à cause du bug pas encore résolu
                response.setUsertype(user.getUsertype());

            } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");

        } else {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "User not found"
            );
        }
        return response;
    }

    /**
     * Renvoi l'utilisateur dont l'id est spécifié dans les entêtes
     * @param userId l'id de l'utilisateur
     * @return L'objet User hydraté
     */
    @GetMapping("/user/me")
    public User me(@RequestHeader("user-id") int userId) {

        Optional<User> opt = this.us.getById(userId);

        if(opt.isPresent()) {

            return opt.get();

        } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

    }

    /**
     * Renvoi la liste des utilisateurs de l'application
     * @param userId l'id de l'utilisateur
     * @return La liste des utilisateurs
     */
    @GetMapping("/users")
    public ArrayList<User> getUsers(@RequestHeader("user-id") int userId) {

        Optional<User> opt = this.us.getById(userId);

        if(opt.isPresent()) {

            if(opt.get().getUsertype().equals(Usertype.ADMIN)) {

                return this.us.all();

            } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    /**
     * Supprime un utilisateur
     * @param userId l'id de l'utilisateur à supprimer
     */
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id, @RequestHeader("user-id") int userId) {

        Optional<User> opt = this.us.getById(userId);

        if(opt.isPresent()) {

            if(opt.get().getUsertype().equals(Usertype.ADMIN)) {

                this.us.deleteUser(id);

            } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    /**
     * Renvoi la liste des utilisateurs de l'application
     * @param userId l'id de l'utilisateur
     * @return L'objet User hydraté
     */
    @PostMapping("/users")
    public void addUser(@RequestBody User newUser, @RequestHeader("user-id") int userId) {

        Optional<User> opt = this.us.getById(userId);

        if(opt.isPresent()) {

            if(opt.get().getUsertype().equals(Usertype.ADMIN)) {

                this.us.create(newUser);

            } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

}
