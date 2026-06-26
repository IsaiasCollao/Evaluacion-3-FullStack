package com.example.bicicleta.ms_auth.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidateResponse {

    private boolean valido;

    private String email;

}