package com.groupe3.api.controller;

import com.groupe3.api.model.*;
import com.groupe3.api.schema.request.LoginRequest;
import com.groupe3.api.schema.response.LoginResponse;
import com.groupe3.api.schema.response.NotifResponse;
import com.groupe3.api.service.ConstructionService;
import com.groupe3.api.service.ProductProviderService;
import com.groupe3.api.service.UserService;
import com.groupe3.api.utils.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

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
    private final ConstructionService cs;
    private final ProductProviderService pps;

    /**
     * Constructor
     * @param us autowired UserService
     * @param sec autowired Security class
     * @param cs autowired ConstructionService class
     * @param pps autowired ProductProviderService class
     * @see Autowired
     */
    @Autowired
    public UserController(UserService us, Security sec, ConstructionService cs, ProductProviderService pps) {
        this.us = us;
        this.security = sec;
        this.cs = cs;
        this.pps = pps;
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

                response.setId(user.getId()); // On n'utilise plus le JWT ?? cause du bug pas encore r??solu
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
     * Renvoi l'utilisateur dont l'id est sp??cifi?? dans les ent??tes
     * @param userId l'id de l'admin qui fait l'action
     * @return L'objet User hydrat??
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
     * @param userId l'id de l'admin qui fait l'action
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
     * @param id l'id de l'utilisateur ?? supprimer
     * @param userId l'id de l'admin qui fait l'action
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
     * @param newUser l'utilisateur ?? ajouter
     * @param userId l'id de l'admin qui fait l'action
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

    @GetMapping("/users/notifs")
    public NotifResponse computeNotification(@RequestHeader("user-id") int userId) {
        NotifResponse nr = new NotifResponse();

        Optional<User> opt = this.us.getById(userId);

        if(opt.isPresent()) {

            if(opt.get().getUsertype().equals(Usertype.ADMIN)) {

                // R??cup??rer les construction au status "WAITING"
                // V??rifier pour chaque produit que la quantit?? est suffisante dans les donn??es
                List<Construction> constructions = this.cs.getConstructionsByState(ConstructionSteps.WAITING);

                var wrapper = new Object(){ String content = ""; };

                constructions.forEach(c -> {
                    for (Map.Entry me: c.getProducts().entrySet()) {
                        Product product = (Product) me.getKey();
                        int quantity = (int)me.getValue();

                        Optional<Product> p = this.pps.getProductById(product.getId().intValue());
                        if(p.isPresent()) {
                            if(p.get().getQuantity().intValue() <= quantity) {
                                wrapper.content += "Le produit " + p.get().getDesignation() + " est bient??t en rupture de stock.\n";
                            }
                        } else {
                            wrapper.content += "Le produit " + product.getDesignation() + " est en rupture de stock. \n";
                        }
                    }
                });


                nr.setContent(wrapper.content);
                System.out.println(nr);

            } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        return nr;
    }
}
