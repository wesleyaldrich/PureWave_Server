package com.purewave.controller;

import com.purewave.model.Post;
import com.purewave.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
        return postService.savePost(null, post, authentication);
    }

    @PostMapping("/{id}")
    public Post createReplyPost(@PathVariable String id, @RequestBody Post post, Authentication authentication) {
        return postService.savePost(id, post, authentication);
    }

    @PutMapping("/{id}")
    public Post editPost(@PathVariable String id, @RequestBody Post updatedPost, Authentication authentication) {
        return postService.editPost(id, updatedPost, authentication);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable String id, Authentication authentication) {

        postService.deletePost(id, authentication);
    }
}
