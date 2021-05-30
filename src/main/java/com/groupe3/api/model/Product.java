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
    private Long price;

    /**
     * Product stock
     */
    private Long quantity;

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
    public Product(Long id, String designation, Long price, Long quantity, Provider provider) {
        this.id = id;
        this.designation = designation;
        this.price = price;
        this.quantity = quantity;
        this.provider = provider;
    }

    /**
     * Constructor surcharge to update product
     * @param id id of existing product
     * @param designation new designation
     * @param price new price
     * @param quantity new quantity
     */
    public Product(Long id, String designation, Long price, Long quantity) {
        this.id = id;
        this.designation = designation;
        this.price = price;
        this.quantity = quantity;
    }
}

