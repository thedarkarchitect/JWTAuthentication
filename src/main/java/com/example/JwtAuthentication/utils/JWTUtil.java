package com.example.JwtAuthentication.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTUtil {
    private static Key getSigningKey() {
         String SECRET = System.getenv("SECRET");
        //this takes a secret of 256bits which is in base10 cause of HMCA Sha algorithm
        byte[] keyBytes = Decoders.BASE64.decode(SECRET); //decode the secret to base64 using the Decorders class
        return Keys.hmacShaKeyFor(keyBytes); //this the key
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody(); //this body contains the the payload information in the token
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimResolvers) {
        final Claims claims = extractAllClaims(token); //Claims constitute the payload of the json web token and represent a set of information exchanged between two parties
        return claimResolvers.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);// this returns the username
    }

    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration); //return the expiration of the token
    }

    private boolean isTokenExpired(String token) {
        long leeway = 1000*60*5;
        return extractExpiration(token).before(new Date(System.currentTimeMillis() - leeway));//this will return a boolean if the token is expired or not
    }

    public boolean isTokenValid(String token) {

        try{
            final String username = extractUsername(token);
            return (username != null && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    private static String generateRefreshToken(Map<String, Object> extractClaims, String email) {
        //this function generates a token by setting the payload, setting the alogrithm used and the expiration dates of the token
        return Jwts.builder().setClaims(extractClaims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 3)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    private static String generateAccessToken(Map<String, Object> extractClaims, String email) {
        //this function generates a token by setting the payload, setting the alogrithm used and the expiration dates of the token
        return Jwts.builder().setClaims(extractClaims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 5)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    public String generateRefreshToken(String email) {
        return generateRefreshToken(new HashMap<>(), email);
    }

    public String generateAccessToken(String email) {
        return generateAccessToken(new HashMap<>(), email);
    }
}
