package com.groupe3.api.schema.response;

import com.groupe3.api.model.Usertype;
import lombok.Data;

@Data
public class LoginResponse {
    public Long id;
    public Usertype usertype;
}
