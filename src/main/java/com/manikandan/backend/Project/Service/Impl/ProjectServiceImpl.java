package com.manikandan.backend.Project.Service.Impl;

import com.manikandan.backend.Project.DTO.Request.ProjectRequestDto;
import com.manikandan.backend.Project.DTO.Response.ProjectResponseDto;
import com.manikandan.backend.Project.Entity.ProjectEntity;
import com.manikandan.backend.Project.Repository.ProjectRepository;
import com.manikandan.backend.Project.Service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    public ProjectResponseDto createProject(ProjectRequestDto request) {
        if (projectRepository.existsById(request.getProjectId())) {
            throw new RuntimeException("Project with ID " + request.getProjectId() + " already exists!");
        }
        ProjectEntity entity = new ProjectEntity(request.getProjectId(), request.getProjectName());
        ProjectEntity savedEntity = projectRepository.save(entity);
        return new ProjectResponseDto(savedEntity.getProjectId(), savedEntity.getProjectName());
    }

    @Override
    public List<ProjectResponseDto> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(entity -> new ProjectResponseDto(entity.getProjectId(), entity.getProjectName()))
                .collect(Collectors.toList());
    }

    @Override
    public ProjectResponseDto getProjectById(String projectId) {
        ProjectEntity entity = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectId));
        return new ProjectResponseDto(entity.getProjectId(), entity.getProjectName());
    }

    @Override
    public List<ProjectResponseDto> getProjectsByName(String projectName) {
        return projectRepository.findByProjectNameContainingIgnoreCase(projectName).stream()
                .map(entity -> new ProjectResponseDto(entity.getProjectId(), entity.getProjectName()))
                .collect(Collectors.toList());
    }

    @Override
    public ProjectResponseDto updateProject(String projectId, ProjectRequestDto request) {
        ProjectEntity entity = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectId));
        
        entity.setProjectName(request.getProjectName());
        
        ProjectEntity updatedEntity = projectRepository.save(entity);
        return new ProjectResponseDto(updatedEntity.getProjectId(), updatedEntity.getProjectName());
    }

    @Override
    public String deleteProject(String projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new RuntimeException("Project not found with ID: " + projectId);
        }
        projectRepository.deleteById(projectId);
        return "Project deleted successfully!";
    }
}
