package com.example.bicicleta.ms_auth.Security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {


    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * Obtiene la llave utilizada para firmar el JWT
     */
    private SecretKey getSignInKey() {

        byte[] keyBytes = Decoders.BASE64.decode(secret);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Genera un JWT utilizando solamente el email
     */
    public String generateToken(String email) {

        return generateToken(new HashMap<>(), email);

    }

    /**
     * Genera un JWT con Claims personalizados
     */
    public String generateToken(
            Map<String, Object> extraClaims,
            String email) {

        Date now = new Date();

        Date expirationDate =
                new Date(now.getTime() + expiration);

        return Jwts.builder()

                .claims(extraClaims)

                .subject(email)

                .issuedAt(now)

                .expiration(expirationDate)

                .signWith(
                        getSignInKey(),
                        SignatureAlgorithm.HS256)

                .compact();

    }

    /**
     * Obtiene el email almacenado dentro del JWT
     */
    public String extractUsername(String token) {

        return extractAllClaims(token)
                .getSubject();

    }

    /**
     * Obtiene todos los Claims del JWT
     */
    public Claims extractAllClaims(String token) {

        return Jwts.parser()

                .verifyWith(getSignInKey())

                .build()

                .parseSignedClaims(token)

                .getPayload();

    }

    /**
     * Obtiene la fecha de expiración
     */
    public Date extractExpiration(String token) {

        return extractAllClaims(token)
                .getExpiration();

    }

    /**
     * Indica si el JWT expiró
     */
    public boolean isTokenExpired(String token) {

        return extractExpiration(token)
                .before(new Date());

    }

    /**
     * Valida que el token pertenezca al usuario
     * y que aún no haya expirado.
     */
    public boolean isTokenValid(
            String token,
            String email) {

        final String username =
                extractUsername(token);

        return username.equals(email)
                && !isTokenExpired(token);

    }

    /**
     * Valida únicamente que el JWT sea correcto.
     * Este método será utilizado por el Gateway.
     */
    public boolean validateToken(String token) {

        try {

            Jwts.parser()

                    .verifyWith(getSignInKey())

                    .build()

                    .parseSignedClaims(token);

            return true;

        }

        catch (JwtException ex) {

            return false;

        }

        catch (Exception ex) {

            return false;

        }

    }
    public String validateAndExtractUsername(String token){

    if(!validateToken(token)){

        throw new RuntimeException(
                "Token inválido");

    }

    return extractUsername(token);

    }
}
