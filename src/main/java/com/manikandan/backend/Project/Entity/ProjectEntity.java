package com.manikandan.backend.Project.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "project_entity")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectEntity {

    @Id
    private String projectId;

    private String projectName;
}
