package com.manikandan.backend.Project.Controller;

import com.manikandan.backend.Project.DTO.Request.ProjectRequestDto;
import com.manikandan.backend.Project.DTO.Response.ProjectResponseDto;
import com.manikandan.backend.Project.Service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponseDto> createProject(@Valid @RequestBody ProjectRequestDto request) {
        return new ResponseEntity<>(projectService.createProject(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponseDto>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDto> getProjectById(@PathVariable String projectId) {
        return ResponseEntity.ok(projectService.getProjectById(projectId));
    }

    @GetMapping("/name/{projectName}")
    public ResponseEntity<List<ProjectResponseDto>> getProjectsByName(@PathVariable String projectName) {
        return ResponseEntity.ok(projectService.getProjectsByName(projectName));
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDto> updateProject(@PathVariable String projectId, @Valid @RequestBody ProjectRequestDto request) {
        return ResponseEntity.ok(projectService.updateProject(projectId, request));
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<String> deleteProject(@PathVariable String projectId) {
        return ResponseEntity.ok(projectService.deleteProject(projectId));
    }
}
