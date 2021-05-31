package com.groupe3.api.service;

import com.groupe3.api.model.Construction;
import com.groupe3.api.model.ConstructionSteps;
import com.groupe3.api.model.Product;
import com.groupe3.api.model.Provider;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service of Chantier class
 * @see Service
 * @author Groupe3
 */
@Service
public class ConstructionService {

    private ArrayList<Construction> constructions;

    private ProductProviderService pps;
    /**
     * Constructor
     * @param pps ProductProviderService autowired service
     */
    @Autowired
    public ConstructionService(ProductProviderService pps) {
        this.constructions = new ArrayList<>();
        this.pps = pps;

        JSONParser jsonParser = new JSONParser();

        try {
            File file = new ClassPathResource("construction_mock.json").getFile();

            FileReader reader = new FileReader(file);

            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray productList = (JSONArray) obj;

            //Iterate over employee array
            productList.forEach(p -> this.parseConstructionsObject((JSONObject) p));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse a jsonobject and return an hydrated object
     * @param us the user json object
     */
    private void parseConstructionsObject(JSONObject us)
    {

        JSONArray products = (JSONArray) us.get("products");
        HashMap<Product, Integer> productList = new HashMap<>();

        products.forEach(p -> {
            JSONObject obj = (JSONObject) p;
            Long id = (Long) obj.get("id");

            Optional<Product> product = this.pps.getProductById(id.intValue());
            if(product.isPresent()) {
                Long quantity = (Long) obj.get("quantity");
                productList.put(product.get(), quantity.intValue() );
            }
        });
        
        this.constructions.add(new Construction(
            (Long) us.get("id"),
            productList,
            (Long) us.get("price"),
            ConstructionSteps.valueOf((String) us.get("state"))
        ));

    }

    /**
     * Create a new Construction in list
     * @param c the construction to add
     */
    public void create(Construction c) {
        c.setId((long)this.constructions.size() + 1);
        this.constructions.add(c);
    }


    /**
     * Retrieve a construction by id
     * @param id construction id
     * @return the construction
     */
    public Optional<Construction> getById(final int id) {
        return this.constructions.stream().filter(u -> u.getId() == id).findFirst();
    }

    /**
     * Return a list of all provider
     * @return providers
     */
    public ArrayList<Construction> getConstructions() {
        return this.constructions;
    }


    /**
     * Update construction in list
     * @param construction updated product
     */
    public void updateConstruction(final Construction construction) {
        for(Construction cons : this.constructions) {
            if(cons.getId().equals(construction.getId())) {
                cons.setPrice(construction.getPrice());
                cons.setProducts(construction.getProducts());
                cons.setState(construction.getState());
                break;
            }
        }
    }

    public List<Construction> getConstructionsByState(final ConstructionSteps step) {
        return this.constructions.stream().filter(c -> c.getState().equals(step)).collect(Collectors.toList());
    }

}
