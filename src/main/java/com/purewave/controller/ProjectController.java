package com.purewave.controller;

import com.purewave.model.Project;
import com.purewave.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/data/projects")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @GetMapping
    public List<Project> getOwnProjects(Authentication authentication) {
        return projectService.getOwnProjects(authentication);
    }

    @PostMapping
    public Project createProject(@RequestBody Project project, Authentication authentication) {
        return projectService.saveProject(project, authentication);
    }
}
