package com.unibank.UnitechAppApplication.model.dto.response;


import lombok.AllArgsConstructor;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginSuccessResponse {

    private String success;
    private String token;
}
