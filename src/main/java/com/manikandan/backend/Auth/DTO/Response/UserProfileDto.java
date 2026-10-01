package com.manikandan.backend.Auth.DTO.Response;

import com.manikandan.backend.Auth.Enums.Role.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {
    private String email;
    private String username;
    private Role role;
    private String image;
}
