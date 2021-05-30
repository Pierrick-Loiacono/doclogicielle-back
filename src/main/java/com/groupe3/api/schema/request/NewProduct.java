package com.groupe3.api.schema.request;

import lombok.Data;

/**
 * Schema du "body" pour l'ajout d'un produit
 * @author Groupe3
 */
@Data
public class NewProduct {

    /**
     * Product id
     */
    private Long id;

    /**
     * Product designation
     */
    private String designation;

    /**
     * Product price
     */
    private Long price;

    /**
     * Product stock
     */
    private Long quantity;

    /**
     * Product supplier
     */
    private int provider;


    /**
     * constructor surcharge
     * @param designation product wording
     * @param price price HT
     * @param quantity quantity left
     * @param provider product supplier
     */
    public NewProduct(String designation, Long price, Long quantity, int provider) {
        this.designation = designation;
        this.price = price;
        this.quantity = quantity;
        this.provider = provider;
    }
}
