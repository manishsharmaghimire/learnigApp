package com.elearn.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {


    private Key key= Keys.hmacShaKeyFor(Base64.getDecoder().decode(JwtConstant.secretKey));



    public String extractUserName(String token){

        return  extractClaim(token,Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key) // use your secret key to verify
                .build()
                .parseClaimsJws(token) // parse the JWT
                .getBody(); // get the claims (data inside token)
    }


    public String generateToken(String username){
        return  createToken(username);
    }

    private String createToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+JwtConstant.jwtExpiration))
                .signWith(key)
                .compact();
    }

      public boolean validateToken(String token, String userName){
          String tokenUsername = extractUserName(token);
          return (userName.equals(tokenUsername)&& !isTokenExpired(token));

      }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());

    }

    private Date extractExpiration(String token) {

       return extractClaim(token,Claims::getExpiration);
    }


}
