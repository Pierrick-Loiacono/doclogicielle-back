package com.groupe3.api.schema.request;

import com.groupe3.api.model.Usertype;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * Class pour une requête d'inscription
 * @author Groupe3
 */
@Data
public class SubscriptionRequest {

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    private String mail;

    @NotEmpty
    private String password;

    @NotEmpty
    private Usertype usertype;

}
