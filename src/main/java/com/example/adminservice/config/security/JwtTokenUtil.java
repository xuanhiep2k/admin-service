package com.example.adminservice.config.security;

import com.example.adminservice.model.CustomUserDetails;
import com.example.adminservice.utils.Constants;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import com.google.gson.Gson;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;

@Component
public class JwtTokenUtil {
    private static final Gson gson = new Gson();

    // retrieve username from jwt token
    public CustomUserDetails getUserFromToken(String token) {
        if (validateToken(token)) {
            JsonObject jo = gson.fromJson(getClaimFromToken(token, Claims::getSubject), JsonObject.class);
            List<GrantedAuthority> authorities = new ArrayList<>();
            JsonArray arr = jo.getAsJsonArray("authorities");
            for (int i = 0; i < arr.size(); i++) {
                authorities.add(new SimpleGrantedAuthority(arr.get(i).getAsJsonObject().get("role").getAsString()));
            }
            List<String> roles = new ArrayList<>();
            if (jo.has("roles")) {
                roles = gson.fromJson(jo.get("roles"), new TypeToken<List<String>>() {
                }.getType());
            }
            String departmentPath = "";
            if (jo.has("departmentPath")) {

                departmentPath = jo.get("departmentPath").getAsString();
            }
            String departmentCode = "";
            if (jo.has("departmentCode")) {
                departmentCode = jo.get("departmentCode").getAsString();
            }
            String partnerCode = "";
            if (jo.has("partnerCode")) {
                partnerCode = jo.get("partnerCode").getAsString();
            }
            return new CustomUserDetails(
                    jo.get("username").getAsString(),
                    "",
                    authorities,
                    jo.get("id").getAsLong(),
                    roles,
                    jo.get("fullName").getAsString(),
                    jo.get("email").getAsString(),
                    departmentCode,
                    partnerCode,
                    departmentPath
            );
        }
        return null;
    }

    public String getTokenFromBearer(String bearerToken) {
        if (StringUtils.hasText(bearerToken)
                && bearerToken.startsWith(Constants.SECURITY.TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // generate token for user
    public String generateToken(CustomUserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, gson.toJson(setCustomUserDetails(userDetails)));
    }

    // set empty password for generate token
    private CustomUserDetails setCustomUserDetails(CustomUserDetails customUserDetails) {
        return new CustomUserDetails(
                customUserDetails.getUsername(),
                "",
                customUserDetails.getAuthorities(),
                customUserDetails.getId(),
                customUserDetails.getRoles(),
                customUserDetails.getFullName(),
                customUserDetails.getEmail(),
                customUserDetails.getDepartmentPath(),
                customUserDetails.getDepartmentCode(),
                customUserDetails.getPartnerCode()
        );
    }

    // while creating the token -
    // 1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    // 2. Sign the JWT using the HS512 algorithm and secret key.
    // 3. According to JWS Compact
    // Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    //   compaction of the JWT to a URL-safe string
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                        + (Constants.SECURITY.EXPIRATION_DAY * 24 * 60 * 60 * 1000)))
                .signWith(SignatureAlgorithm.HS512, Constants.SECURITY.SECRET)
                .compact();
    }

    // retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(Constants.SECURITY.SECRET).parseClaimsJws(token).getBody();
    }

    // check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // validate token
    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }
}
