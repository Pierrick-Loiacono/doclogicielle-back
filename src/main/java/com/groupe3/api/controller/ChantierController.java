package com.groupe3.api.controller;

import com.groupe3.api.model.Product;
import com.groupe3.api.model.Provider;
import com.groupe3.api.model.User;
import com.groupe3.api.model.Usertype;
import com.groupe3.api.schema.request.NewProduct;
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
     * Constructor
     * @see Autowired
     */
    @Autowired
    public ChantierController(UserService us) {
        this.us = us;
    }
}
