package com.groupe3.api.model;

import lombok.Data;

/**
 * Model for a User
 */
@Data
public class User {

    /**
     * User id
     */
    private Long id;

    /**
     * User first name
     */
    private String firstName;

    /**
     * User last name
     */
    private String lastName;

    /**
     * User email
     */
    private String mail;

    /**
     * User encrypted password
     * @see org.mindrot.jbcrypt.BCrypt
     */
    private String password;

    /**
     * User type for this
     */
    private Usertype usertype;

    /**
     * Default constructor
     */
    public User() { }

    /**
     * Constructor surcharge
     * @param firstName first name
     * @param lastName last name
     * @param mail email
     * @param password encoded passwor
     * @param usertype user type
     */
    public User(String firstName, String lastName, String mail, String password, Usertype usertype) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.password = password;
        this.usertype = usertype;
    }

    /**
     * Constructor surcharge
     * @param id id
     * @param firstName first name
     * @param lastName last name
     * @param mail email
     * @param password encoded password
     * @param usertype user type
     */
    public User(Long id, String firstName, String lastName, String mail, String password, Usertype usertype) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.password = password;
        this.usertype = usertype;
    }

    public boolean isEmpty() {
        return
            this.firstName == "" &&
            this.lastName == "" &&
            this.mail == "" &&
            this.password == "";
    }
    @Override
    public String toString() {
        return this.firstName + " " + this.lastName;
    }
}
