package com.manikandan.backend.Auth.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@Service
public class JwtService {

    private final Set<String> blacklistedTokens = new HashSet<>();

    @Value("${jwt.private.key}")
    private String privateKeyStr;

    @Value("${jwt.public.key}")
    private String publicKeyStr;

    @Value("${jwt.access.expiration}")
    private long jwtExpiration;

    @Value("${jwt.refresh.expiration}")
    private long refreshExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(String username, String fingerprint) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("fgp", fingerprint);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getPrivateKey(), SignatureAlgorithm.ES256)
                .compact();
    }

    public String generateRefreshToken(String username, String fingerprint) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("fgp", fingerprint);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(getPrivateKey(), SignatureAlgorithm.ES256)
                .compact();
    }

    public boolean isTokenValid(String token, String username) {
        if (blacklistedTokens.contains(token)) {
            return false;
        }
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username)) && !isTokenExpired(token);
    }
    
    public boolean validateFingerprint(String token, String cookieFingerprint) {
        String tokenFgp = extractClaim(token, claims -> claims.get("fgp", String.class));
        if (tokenFgp == null) return false;
        return tokenFgp.equals(cookieFingerprint);
    }

    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private PrivateKey getPrivateKey() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(privateKeyStr);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("EC");
            return kf.generatePrivate(spec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read Private Key", e);
        }
    }

    private PublicKey getPublicKey() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(publicKeyStr);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("EC");
            return kf.generatePublic(spec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read Public Key", e);
        }
    }
}
