package com.unibank.UnitechAppApplication.model.dto.request;

import lombok.Data;

@Data
public class AuthorRequest {
    private String pin;
    private String password;
}
