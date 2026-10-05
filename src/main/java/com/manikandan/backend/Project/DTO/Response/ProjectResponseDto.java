package com.manikandan.backend.Project.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponseDto {
    private String projectId;
    private String projectName;
}
