package com.groupe3.api.model;

import lombok.Data;
import org.apache.tomcat.util.bcel.Const;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Model for a construction such as house, building
 * @see Data
 */
@Data
public class Construction {

    /**
     * Construction id
     */
    private Long id;

    /**
     * Dictionnary that represent which product the construction needs
     * and the quantity for each product
     */
    private HashMap<Product, Integer> products;

    /**
     * Price for a construction
     */
    private Long price;

    /**
     * State of construction
     */
    private ConstructionSteps state;

    /**
     * Constructor
     * @param id id of construction
     * @param products list of products
     * @param price total cost
     * @param state state of construction
     */
    public Construction(Long id, HashMap<Product, Integer> products, Long price, ConstructionSteps state) {
        this.id = id;
        this.products = products;
        this.price = price;
        this.state = state;
    }

}
