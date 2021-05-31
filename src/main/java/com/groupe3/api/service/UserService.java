package com.groupe3.api.service;

import com.groupe3.api.model.User;
import com.groupe3.api.model.Usertype;
import com.groupe3.api.utils.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Service of User class
 * @see Service
 * @author Groupe3
 */
@Service
public class UserService {

    private ArrayList<User> users;
    private Security security;

    /**
     * Constructor
     * @param security Security service
     */
    @Autowired
    public UserService(Security security) {
        this.security = security;
        this.users = new ArrayList<>();

        JSONParser jsonParser = new JSONParser();

        try {
            File file = new ClassPathResource("user_mock.json").getFile();

            FileReader reader = new FileReader(file);

            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray userList = (JSONArray) obj;

            //Iterate over employee array
            userList.forEach(user -> this.users.add(parseUserObject((JSONObject) user)));

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
    private static User parseUserObject(JSONObject us)
    {
        return new User(
            (Long) us.get("id"),
            (String) us.get("firstname"),
            (String) us.get("lastname"),
            (String) us.get("mail"),
            Security.hashPassword((String)us.get("password")),
            Usertype.ADMIN.toString().equals(us.get("usertype")) ? Usertype.ADMIN : Usertype.USER
        );
    }

    /**
     * Create a new user in database
     * @param user the user to add
     */
    public void create(User user) {
        user.setId((long) (this.users.size() + 1));
        user.setPassword(Security.hashPassword(user.getPassword()));
        this.users.add(user);
    }

    /**
     * Find a user by mail
     * @param email the mail to find in database
     * @return An optional object that may contains the retrieved user
     */
    public Optional<User> retrieveUser(final String email) {
        Optional<User> user = this.users.stream().filter(u -> u.getMail().equals(email)).findFirst();
        return user;
    }

    /**
     * Retrieve a user by id
     * @param id user id
     * @return the user
     */
    public Optional<User> getById(final int id) {
        return this.users.stream().filter(u -> u.getId() == id).findFirst();
    }


    /**
     * Return a list of all users
     * @return all users
     */
    public ArrayList<User> all() {
        return this.users;
    }

    /**
     * Delete a user
     * @param id user id to delete
     */
    public void deleteUser(final int id) {
        this.users.removeIf(u -> u.getId() == id);
    }
}
