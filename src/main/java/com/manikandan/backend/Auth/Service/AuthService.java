package com.manikandan.backend.Auth.Service;

import com.manikandan.backend.Auth.DTO.Request.UserRequestDto;
import com.manikandan.backend.Auth.DTO.Response.UserResponseDto;

public interface AuthService {
    UserResponseDto login(UserRequestDto request, String fingerprint);
    UserResponseDto refreshToken(String refreshToken, String fingerprint);
    void logout(String token);
    com.manikandan.backend.Auth.DTO.Response.UserProfileDto getProfile(String email);
}
