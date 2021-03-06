package com.ecommerce.config.security;

import com.ecommerce.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class TokenService {

    @Value("${ecommerce.jwt.expiration}")
    private String expiration;

    @Value("${ecommerce.jwt.secret}")
    private String secret;

    public String generateToken(Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        Date today = new Date();
        Date expirationDate = new Date(today.getTime() + Long.parseLong(expiration));

        return Jwts.builder()
                .setIssuer("API ecommerce")
                .setSubject(user.getId().toString())
                .setIssuedAt(today)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

    }

    public Long getUserById(String token) {

        try {

            Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
            return Long.parseLong(claims.getSubject());

        } catch (ExpiredJwtException e) {

            throw new CredentialsExpiredException("Expired jwt credentials ", e);

        }
        /*catch (Exception e) {

            //log.info("JWT token compact of handler are invalid.");
            //log.trace("JWT token compact of handler are invalid trace: ", e);
        }*/
    }
}
