package com.manikandan.backend.Auth.Controller;

import com.manikandan.backend.Auth.DTO.Request.UserRequestDto;
import com.manikandan.backend.Auth.DTO.Response.UserResponseDto;
import com.manikandan.backend.Auth.Service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Value("${app.cookie.secure}")
    private boolean cookieSecure;

    @Value("${app.cookie.same-site}")
    private String cookieSameSite;

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@Valid @RequestBody UserRequestDto request, HttpServletRequest httpRequest, HttpServletResponse response) {
        
        String fingerprint = UUID.randomUUID().toString();
        
        UserResponseDto responseDto = authService.login(request, fingerprint);
        
        setFingerprintCookie(response, fingerprint);
        
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<UserResponseDto> refresh(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }
        String refreshToken = authHeader.substring(7);
        
        String cookieFgp = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("Fingerprint".equals(cookie.getName())) {
                    cookieFgp = cookie.getValue();
                }
            }
        }

        UserResponseDto responseDto = authService.refreshToken(refreshToken, cookieFgp);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return ResponseEntity.badRequest().body("Error: You are not logged in to logout!");
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            authService.logout(token); 
        }
        
        setFingerprintCookie(response, null);

        return ResponseEntity.ok("Logout Successful");
    }

    private void setFingerprintCookie(HttpServletResponse response, String fingerprint) {
        long maxAge = (fingerprint == null) ? 0 : 7 * 24 * 60 * 60;
        
        ResponseCookie cookie = ResponseCookie.from("Fingerprint", fingerprint != null ? fingerprint : "")
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .maxAge(maxAge)
                .sameSite(cookieSameSite)
                .build();
                
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
