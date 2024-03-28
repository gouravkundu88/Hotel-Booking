package com.hotel.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.hotel.entity.PropertyUser;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService {
    @Value("${jwt.alorithm.key}")
    private String algorithmKey;
    @Value("${jwt.issuer.details}")
    private String issuer;
    @Value("${jwt.expiry.duration}")
    private int expiryTime;

    private Algorithm algorithm;

    private final static String USER_NAME="username";

    @PostConstruct
    public void postContruct(){
        algorithm = Algorithm.HMAC256(algorithmKey);
    }
    //For creating JWT-token use this line of code("Remember CEIS" flow)
    public String generateToken(PropertyUser propertyUser){
        return JWT.create()
                .withClaim(USER_NAME,propertyUser.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+expiryTime))
                .withIssuer(issuer)
                .sign(algorithm);
    }

}
