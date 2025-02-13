package com.purewave.controller;

import com.purewave.model.Project;
import com.purewave.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/data/projects")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @GetMapping
    public List<Project> getOwnProjects(Authentication authentication) {
        return projectService.getOwnProjects(authentication);
    }

    @GetMapping("/{accessId}")
    public Optional<Project> openProject(@PathVariable String accessId) {
        return projectService.openProject(accessId);
    }

    @PostMapping
    public Project createProject(@RequestBody Project project, Authentication authentication) {
        return projectService.saveProject(project, authentication);
    }
}
