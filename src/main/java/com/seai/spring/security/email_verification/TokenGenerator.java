package com.seai.spring.security.email_verification;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.math.BigInteger;

@Component
public class TokenGenerator {
    public String generateToken() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }
}