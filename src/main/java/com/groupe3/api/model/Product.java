package com.groupe3.api.model;

import lombok.Data;

/**
 * Model for a product
 */
@Data
public class Product {

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
    private float price;

    /**
     * Product stock
     */
    private int quantity;

    /**
     * Product supplier
     */
    private Provider provider;

    /**
     * Default constructor
     */
    public Product() { }

    /**
     * constructor surcharge
     * @param id id
     * @param designation product wording
     * @param price price HT
     * @param quantity quantity left
     * @param provider product supplier
     */
    public Product(Long id, String designation, float price, int quantity, Provider provider) {
        this.id = id;
        this.designation = designation;
        this.price = price;
        this.quantity = quantity;
        this.provider = provider;
    }
}

