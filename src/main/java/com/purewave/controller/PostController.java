package com.purewave.controller;

import com.purewave.model.Post;
import com.purewave.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/data/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping
    public List<Post> getPrimaryPosts() {
        return postService.getPrimaryPosts();
    }

    @GetMapping("/{id}")
    public List<Post> getRepliesByPostId(@PathVariable String id) {
        return postService.getRepliesByPostId(id);
    }

    @PostMapping
    public Post createPost(@RequestBody Post post, Authentication authentication) {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");
        String picture = oauthUser.getAttribute("picture");

        post.setUserId(email);
        post.setName(name);
        post.setPicture(picture);
        post.setAttachedTo(null);
        // TO DO: attachment

        return postService.savePost(post);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable String id) {
        postService.deletePost(id);
    }
}
