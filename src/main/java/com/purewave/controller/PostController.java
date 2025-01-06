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
        return postService.savePost(post, authentication, null);
    }

    @PostMapping("/{id}")
    public Post createReplyPost(@RequestBody Post post, Authentication authentication, @PathVariable String id) {
        return postService.savePost(post, authentication, id);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable String id) {
        postService.deletePost(id);
    }
}
