package com.bicicletas.api_gateway.Security;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RouteValidator {

    public static final List<String> OPEN_ENDPOINTS = List.of(

            "/api/auth/login",

            "/api/auth/validate"

    );

    public boolean isPublic(String path,
                            HttpMethod method){

        if(path.equals("/api/auth/login"))

            return true;

        if(path.equals("/api/auth/validate"))

            return true;

        if(path.equals("/api/usuarios")
                && method == HttpMethod.POST)

            return true;

        return false;

    }

}