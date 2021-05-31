package com.groupe3.api.controller;

import com.groupe3.api.model.*;
import com.groupe3.api.schema.request.NewProduct;
import com.groupe3.api.service.ConstructionService;
import com.groupe3.api.service.ProductProviderService;
import com.groupe3.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Controller that contains endpoints relative "chantier"
 * @author Groupe3
 * @see User
 * @see RestController
 */
@RestController
public class ChantierController {

    /**
     * Service des users
     */
    private final UserService us;

    /**
     * Service des constructions
     */
    private final ConstructionService cs;


    /**
     * Constructor
     * @see Autowired
     */
    @Autowired
    public ChantierController(UserService us, ConstructionService cs) {
        this.us = us;
        this.cs = cs;
    }

    @GetMapping("/constructions")
    public ArrayList<Construction> getConstructions(@RequestHeader("user-id") int userId) {

        Optional<User> opt_user = this.us.getById(userId);

        if(opt_user.isPresent()) {

            // Seuls les admins on le droit de voir les chantiers
            if(opt_user.get().getUsertype() == Usertype.ADMIN) {

               return this.cs.getConstructions();

            } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

}
