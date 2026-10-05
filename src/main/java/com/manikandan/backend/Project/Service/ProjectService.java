package com.manikandan.backend.Project.Service;

import com.manikandan.backend.Project.DTO.Request.ProjectRequestDto;
import com.manikandan.backend.Project.DTO.Response.ProjectResponseDto;

import java.util.List;

public interface ProjectService {
    ProjectResponseDto createProject(ProjectRequestDto request);
    List<ProjectResponseDto> getAllProjects();
    ProjectResponseDto getProjectById(String projectId);
    List<ProjectResponseDto> getProjectsByName(String projectName);
    ProjectResponseDto updateProject(String projectId, ProjectRequestDto request);
    String deleteProject(String projectId);
}
