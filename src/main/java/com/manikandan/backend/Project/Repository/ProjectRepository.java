package com.manikandan.backend.Project.Repository;

import com.manikandan.backend.Project.Entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, String> {
    
    List<ProjectEntity> findByProjectNameContainingIgnoreCase(String projectName);
}
