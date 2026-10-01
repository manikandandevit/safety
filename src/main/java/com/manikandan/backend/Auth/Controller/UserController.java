package com.manikandan.backend.Auth.Controller;

import com.manikandan.backend.Auth.DTO.Response.UserProfileDto;
import com.manikandan.backend.Auth.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getMyProfile(Authentication authentication) {
        String email = authentication.getName(); 
        
        return ResponseEntity.ok(authService.getProfile(email));
    }
}
