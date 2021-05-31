package com.groupe3.api.service;

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
 * Service of Product and provider class
 * @see Service
 * @author Groupe3
 */
@Service
public class ProductProviderService {

    private ArrayList<Product> products;
    private ArrayList<Provider> providers;

    /**
     * Constructor
     */
    public ProductProviderService() {
        this.products = new ArrayList<>();
        this.providers = new ArrayList<>();

        JSONParser jsonParser = new JSONParser();

        try {
            File file = new ClassPathResource("product_mock.json").getFile();

            FileReader reader = new FileReader(file);

            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray productList = (JSONArray) obj;

            //Iterate over employee array
            productList.forEach(p -> this.parseProductObject((JSONObject) p));

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
    private void parseProductObject(JSONObject us)
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
     * Create a new product in database
     * @param product the product to add
     */
    public void create(Product product) {
        product.setId((long)this.products.size() + 1);
        this.products.add(product);
    }

    /**
     * Create a new provider in database
     * @param provider the provider to add
     */
    public void create(Provider provider) {
        provider.setId((long)this.providers.size() + 1);
        this.providers.add(provider);
    }

    /**
     * Retrieve a product by id
     * @param id product_id
     * @return the product
     */
    public Optional<Product> getProductById(final int id) {
        return this.products.stream().filter(u -> u.getId() == id).findFirst();
    }

    /**
     * Retrieve a provider by id
     * @param id provider_id
     * @return the provider
     */
    public Optional<Provider> getProviderById(final int id) {
        return this.providers.stream().filter(u -> u.getId() == id).findFirst();
    }

    /**
     * Return a list of all provider
     * @return providers
     */
    public ArrayList<Provider> getProviders() {
        return this.providers;
    }

    /**
     * Return a list of all products
     * @return products
     */
    public ArrayList<Product> getProducts() {
        return this.products;
    }

    /**
     * Delete a product on the list
     * @param productId product id to delete
     */
    public void deleteProduct(final long productId) {
        this.products.removeIf(p -> p.getId() == productId);
    }

    /**
     * Delete a provider on the list
     * @param providerId provider id to delete
     */
    public void deleteProvider(final long providerId) {
        this.providers.removeIf(p -> p.getId() == providerId);
    }

    /**
     * Update product in list
     * @param product updated product
     */
    public void updateProduct(final Product product) {
        for(Product p : this.products) {
            if(p.getId().equals(product.getId())) {
                p.setDesignation(product.getDesignation());
                p.setPrice(product.getPrice());
                p.setQuantity(product.getQuantity());
                break;
            }
        }
    }

    /**
     * Update product in list
     * @param provider updated product
     */
    public void updateProvider(final Provider provider) {
        System.out.println(provider);
        for(Provider p : this.providers) {
            if(p.getId().equals(provider.getId())) {
                p.setAddress(provider.getAddress());
                p.setFirmName(provider.getFirmName());
                p.setPhone(provider.getPhone());
                break;
            }
        }
    }

}
