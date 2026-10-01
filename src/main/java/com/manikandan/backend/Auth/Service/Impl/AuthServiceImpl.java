package com.manikandan.backend.Auth.Service.Impl;

import com.manikandan.backend.Auth.DTO.Request.UserRequestDto;
import com.manikandan.backend.Auth.DTO.Response.UserResponseDto;
import com.manikandan.backend.Auth.Entity.UserEntity;
import com.manikandan.backend.Auth.Repository.UserRepository;
import com.manikandan.backend.Auth.Security.JwtService;
import com.manikandan.backend.Auth.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public UserResponseDto login(UserRequestDto request, String fingerprint) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        var accessToken = jwtService.generateToken(user.getUsername(), fingerprint);
        var refreshToken = jwtService.generateRefreshToken(user.getUsername(), fingerprint);

        return new UserResponseDto(
                "Login Successful",
                accessToken,
                refreshToken,
                user.getUsername(),
                user.getRole()
        );
    }

    @Override
    public UserResponseDto refreshToken(String refreshToken, String fingerprint) {
        String username = jwtService.extractUsername(refreshToken);
        if (username != null) {
            var user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            if (jwtService.isTokenValid(refreshToken, user.getUsername()) && jwtService.validateFingerprint(refreshToken, fingerprint)) {
                
                var newAccessToken = jwtService.generateToken(user.getUsername(), fingerprint);
                
                return new UserResponseDto(
                    "Token Refreshed",
                    newAccessToken,
                    refreshToken, 
                    user.getUsername(),
                    user.getRole()
                );
            }
        }
        throw new RuntimeException("Invalid Refresh Token or Fingerprint Mismatch");
    }

    @Override
    public void logout(String token) {
        jwtService.blacklistToken(token);
    }

    @Override
    public com.manikandan.backend.Auth.DTO.Response.UserProfileDto getProfile(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
                
        return new com.manikandan.backend.Auth.DTO.Response.UserProfileDto(
                user.getEmail(),
                user.getUsername(),
                user.getRole(),
                user.getImage()
        );
    }
}
