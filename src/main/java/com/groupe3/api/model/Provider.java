package com.groupe3.api.model;

import lombok.Data;

/**
 * Model for a supplier
 * @see Data
 */
@Data
public class Provider {

    /**
     * Provider Id
     */
    private Long id;

    /**
     * Provider firm name
     */
    private String firmName;

    /**
     * Provider address
     */
    private String address;

    /**
     * Provider phone number
     */
    private String phone;

    /**
     * Default constructor
     */
    public Provider() { }

    /**
     * Constructor surcharge
     * @param id id
     * @param firmName first name
     * @param address address
     * @param phone phone number
     */
    public Provider(Long id, String firmName, String address, String phone) {
        this.id = id;
        this.firmName = firmName;
        this.address = address;
        this.phone = phone;
    }

    /**
     * Constructor surchage for new provider
     * @param firmName society name
     * @param address address
     * @param phone phone number
     */
    public Provider(String firmName, String address, String phone) {
        this.firmName = firmName;
        this.address = address;
        this.phone = phone;
    }
}
