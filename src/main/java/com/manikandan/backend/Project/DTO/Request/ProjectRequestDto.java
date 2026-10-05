package com.manikandan.backend.Project.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ProjectRequestDto {

    @NotBlank(message = "Project ID is required")
    private String projectId;

    @NotBlank(message = "Project Name is required")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Project Name must contain only alphabets")
    private String projectName;
}
