package com.bicicletas.api_gateway.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getSignInKey() {

        byte[] keyBytes = Decoders.BASE64.decode(secret);

        return Keys.hmacShaKeyFor(keyBytes);

    }

    public Claims extractClaims(String token) {

        return Jwts.parser()

                .verifyWith(getSignInKey())

                .build()

                .parseSignedClaims(token)

                .getPayload();

    }

    public String extractUsername(String token) {

        return extractClaims(token)

                .getSubject();

    }

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

}