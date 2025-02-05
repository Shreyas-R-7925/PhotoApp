package com.appsdeveloperblog.app.ws.mobile_app_ws.shared.utils;

import com.appsdeveloperblog.app.ws.mobile_app_ws.security.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

@Component
public class Utils {

    private final Random RANDOM = new SecureRandom();
    private final String ALPHABET = "0123456789QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm";

    public String generateUserId(int length){
        return generateRandomString(length);
    }

    private String generateRandomString(int length){

        StringBuilder returnValue = new StringBuilder(length);

        for(int i = 0; i < length; i++){
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }

        return new String(returnValue);
    }

    public static boolean hasTokenExpired(String token){

        boolean returnValue = false;

        try {
//            Claims claims = Jwts.parser()
//                .setSigningKey(SecurityConstants.getTokenSecret())
//                .parseClaimsJws(token).getBody();
            Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.getTokenSecret())
                .build()
                .parseClaimsJws(token)
                .getBody();


            Date tokenExpirationDate = claims.getExpiration();
            Date todayDate = new Date();

            returnValue = tokenExpirationDate.before(todayDate);
        }catch (ExpiredJwtException ex){
            returnValue = true;
        }

        return returnValue;

    }

}
