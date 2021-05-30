package com.groupe3.api.service;

import com.groupe3.api.model.Construction;
import com.groupe3.api.model.Product;
import com.groupe3.api.model.Provider;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Service of Chantier class
 * @see Service
 * @author Groupe3
 */
@Service
public class ConstructionService {

    private ArrayList<Construction> constructions;

    /**
     * Constructor
     */
    public ConstructionService() {
        this.constructions = new ArrayList<>();

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

        JSONObject provider = (JSONObject) us.get("provider");

        this.providers.add(new Provider(
                (Long) provider.get("id"),
                (String) provider.get("firmName"),
                (String) provider.get("address"),
                (String) provider.get("phone")
        ));

        this.products.add(new Product(
                (Long) us.get("id"),
                (String) us.get("designation"),
                (Long) us.get("price"),
                (Long) us.get("quantity"),
                this.providers.get(0)
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

}
