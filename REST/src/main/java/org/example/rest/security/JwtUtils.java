package org.example.rest.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.example.rest.exceptions.PreconditionException;
import org.example.rest.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.naming.PartialResultException;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

@Component
public class JwtUtils {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int expiration_time;

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .subject(authentication.getName())
                .claim("auth", authorities)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + expiration_time))
                .signWith(key())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public String parseUsername(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build().parseSignedClaims(token)
                .getPayload().getSubject();
    }

    public String parseAuth(String token) {
        return (String) Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("auth");
    }

    public String parseJwt(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String generateTokenUpdate(User user) {
        return Jwts.builder()
                .subject("user-data")
                .claims(java.util.Map.of(
                        "id", user.getId(),
                        "username", user.getUsername(),
                        "active", user.isActive(),
                        "role", user.getRole()
                ))
                .signWith(key())
                .compact();
    }

    public void verifyUpdate(String ifMatch, User existingUser) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build().parseSignedClaims(ifMatch).getPayload();

            if (!generateTokenUpdate(existingUser).equals(ifMatch)) {
                throw new PreconditionException("Invalid signature or modified data");
            }

           /* if (!claims.get("id").equals(existingUser.getId()) ||
                    !claims.get("username").equals(existingUser.getUsername()) ||
                    !claims.get("active").equals(existingUser.isActive()) ||
                    !claims.get("role").equals(existingUser.getRole().toString())) {
                throw new PreconditionException("Invalid signature or modified data");
            }*/
        } catch (Exception e) {
            throw new PreconditionException(e.getMessage());
//            throw new PreconditionException("Invalid signature");
        }
    }
}