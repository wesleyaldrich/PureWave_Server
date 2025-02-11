package com.purewave.service;

import com.purewave.model.Project;
import com.purewave.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    public List<Project> getOwnProjects(Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        return projectRepository.findByUserId(email);
    }

    public Optional<Project> getProjectById(String id) {
        return projectRepository.findById(id);
    }

    public List<Project> getProjectsByUserId(String userId) {
        return projectRepository.findByUserId(userId);
    }

    public Project saveProject(Project project, Authentication authentication) {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");

        String accessId = getSaltString();
        project.setAccessId(accessId);
        project.setUserId(email);

        System.out.println("Project before saving: " + project);

        return projectRepository.save(project);
    }

    private String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();

        while (salt.length() < 12) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }

        return salt.toString();
    }

    public void deleteProject(String id) {
        projectRepository.deleteById(id);
    }
}
