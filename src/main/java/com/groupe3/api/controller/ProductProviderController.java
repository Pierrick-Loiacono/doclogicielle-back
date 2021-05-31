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
 * Controller that contains endpoints relative to users and authentication
 * @author Groupe3
 * @see User
 * @see RestController
 */
@RestController
public class ProductProviderController {

    /**
     * Service des produits
     */
    private final ProductProviderService ps;

    /**
     * Service des users
     */
    private final UserService us;

    /**
     * Constructor
     * @see Autowired
     */
    @Autowired
    public ProductProviderController(ProductProviderService ps, UserService us) {
        this.ps = ps;
        this.us = us;
    }

    /**
     * Renvoi le produit spécifié dans l'url, (visible par admins et users)
     * @param id l'id du produit
     * @param userId l'id de l'utilisateur
     * @return L'objet User hydraté
     */
    @GetMapping("/product/{id}")
    public Product getProduct(@PathVariable int id, @RequestHeader("user-id") int userId) {

        Optional<User> opt_user = this.us.getById(userId);
        if(opt_user.isPresent()) {

            Optional<Product> opt_produit = this.ps.getProductById(id);

            if(opt_produit.isPresent()) {

                return opt_produit.get();

            } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    /**
     * Renvoi la listed des produit (visible par admins et users)
     * @param userId l'id de l'utilisateur
     * @return liste des produits
     */
    @GetMapping("/product")
    public ArrayList<Product> getProducts(@RequestHeader("user-id") int userId) {

        Optional<User> opt_user = this.us.getById(userId);
        if(opt_user.isPresent()) {
            return this.ps.getProducts();

        } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    /**
     * Ajoute un produit
     * @param np Schema hydraté pour l'ajout d'un produit
     * @param userId l'id de l'utilisateur
     */
    @PostMapping("/product")
    public void addProduct(@RequestBody NewProduct np, @RequestHeader("user-id") int userId) {

        Optional<User> opt_user = this.us.getById(userId);

        if(opt_user.isPresent()) {

            // Seuls les admins on le droit d'ajouter des produits
            if(opt_user.get().getUsertype().equals(Usertype.ADMIN)) {

                Product product = new Product();
                product.setPrice(np.getPrice());
                product.setDesignation(np.getDesignation());
                product.setQuantity(np.getQuantity());

                Optional<Provider> opt_provider = this.ps.getProviderById(np.getProvider());

                // Vérifie que le produit a bien un provider existant
                if(opt_provider.isPresent()) { product.setProvider(opt_provider.get()); } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provider is not allowed");

                // Ajoute le produit
                this.ps.create(product);

            } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    /**
     * Supprime un produit
     * @param productId l'id du produit à supprimer
     * @param userId l'id de l'utilisateur
     */
    @DeleteMapping("/product/{productId}")
    public void deleteProduct(@PathVariable int productId, @RequestHeader("user-id") int userId) {

        Optional<User> opt_user = this.us.getById(userId);

        if(opt_user.isPresent()) {

            // Seuls les admins on le droit d'ajouter des produits
            if(opt_user.get().getUsertype() == Usertype.ADMIN) {

                Optional<Product> opt_produit = this.ps.getProductById(productId);

                // Vérifie que le produit a bien un provider existant
                if(opt_produit.isPresent()) {
                    this.ps.deleteProduct(opt_produit.get().getId());

                } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");

            } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    /**
     * Update le produit spécifié de l'url
     * @param id id du produit
     * @param updated nouveau produit
     * @param userId id de l'utilisateur courant
     */
    @PutMapping("/product/{id}")
    public void updateProduct(
        @PathVariable int id,
        @RequestBody Product updated,
        @RequestHeader("user-id") int userId
    ) {

        Optional<User> opt_user = this.us.getById(userId);

        if(opt_user.isPresent()) {

            // Seuls les admins on le droit d'ajouter des produits
            if(opt_user.get().getUsertype() == Usertype.ADMIN) {

                Optional<Product> opt_produit = this.ps.getProductById(id);

                if(opt_produit.isPresent()) {

                    this.ps.updateProduct(updated);

                } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");

            } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    /**
     * Renvoir le provider spécifié dans l'URL
     * @param id l'id du provider
     * @param userId l'id de l'utilisateur
     * @return L'objet provider hydraté
     */
    @GetMapping("/provider/{id}")
    public Provider getProvider(@PathVariable int id, @RequestHeader("user-id") int userId) {

        Optional<User> opt_user = this.us.getById(userId);
        if(opt_user.isPresent()) {

            Optional<Provider> opt_provider = this.ps.getProviderById(id);

            if(opt_provider.isPresent()) {

                return opt_provider.get();

            } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    /**
     * Renvoi la liste des providers
     * @param userId l'id de l'utilisateur
     * @return L'objet User hydraté
     */
    @GetMapping("/provider")
    public ArrayList<Provider> getProviders(@RequestHeader("user-id") int userId) {

        Optional<User> opt_user = this.us.getById(userId);
        if(opt_user.isPresent()) {

            return this.ps.getProviders();

        } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }


    /**
     * Ajoute un provider dans la liste
     * @param np Schema hydraté pour l'ajout d'un provider
     * @param userId l'id de l'utilisateur
     */
    @PostMapping("/provider")
    public void addProvider(@RequestBody Provider np, @RequestHeader("user-id") int userId) {

        Optional<User> opt_user = this.us.getById(userId);

        if(opt_user.isPresent()) {

            // Seuls les admins on le droit d'ajouter des providers
            if(opt_user.get().getUsertype().equals(Usertype.ADMIN)) {

                this.ps.create(np);

            } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    /**
     * Supprime le provider
     * @param providerId l'id du provider à supprimer
     * @param userId l'id de l'utilisateur
     */
    @DeleteMapping("/provider/{providerId}")
    public void deleteProvider(@PathVariable int providerId, @RequestHeader("user-id") int userId) {

        Optional<User> opt_user = this.us.getById(userId);

        if(opt_user.isPresent()) {

            // Seuls les admins on le droit de supprimer des provider
            if(opt_user.get().getUsertype() == Usertype.ADMIN) {

                Optional<Provider> opt_provider = this.ps.getProviderById(providerId);

                // Vérifie que le produit a bien un provider existant
                if(opt_provider.isPresent()) {

                    this.ps.deleteProvider(opt_provider.get().getId());

                } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");

            } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    /**
     * Update un provider
     * @param id id du provider
     * @param updated nouveau provider
     * @param userId id de l'utilisateur courant
     */
    @PutMapping("/provider/{id}")
    public void updateProvider(
        @PathVariable int id,
        @RequestBody Provider updated,
        @RequestHeader("user-id") int userId
    ) {

        Optional<User> opt_user = this.us.getById(userId);

        if(opt_user.isPresent()) {

            // Seuls les admins on le droit d'update des provider
            if(opt_user.get().getUsertype() == Usertype.ADMIN) {

                Optional<Provider> opt_provider = this.ps.getProviderById(id);

                if(opt_provider.isPresent()) {
                    this.ps.updateProvider(updated);

                } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Provider not found");

            } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
}
