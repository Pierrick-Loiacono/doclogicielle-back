package com.groupe3.api.schema.request;

import lombok.Data;

@Data
public class LoginRequest {
    private final String mail;
    private final String password;
}
