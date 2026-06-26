package com.bicicletas.api_gateway.DTO;

import lombok.Data;

@Data
public class ValidateResponse {

    private boolean valido;

    private String email;

}