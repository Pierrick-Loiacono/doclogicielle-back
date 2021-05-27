package com.groupe3.api.model;

import lombok.Data;

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
    private float price;

    /**
     * State of construction
     */
    private ConstructionSteps state;

}
