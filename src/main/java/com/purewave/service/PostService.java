package com.purewave.service;

import com.purewave.model.Post;
import com.purewave.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    // Fetch only primary posts (attachedTo == null)
    @Cacheable(value = "posts")
    public List<Post> getPrimaryPosts() {
        return postRepository.findByAttachedToIsNull();
    }

    @Cacheable(value = "posts", key = "#postId")
    // Fetch replies for a given post ID
    public List<Post> getRepliesByPostId(String postId) {
        return postRepository.findByAttachedTo(postId);
    }

    @CachePut(value = "posts", key = "#result.id")
    public Post savePost(@RequestBody Post post, Authentication authentication, String id) {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");
        String picture = oauthUser.getAttribute("picture");

        post.setUserId(email);
        post.setName(name);
        post.setPicture(picture);

        post.setAttachedTo(id);

        return postRepository.save(post);
    }

    @CacheEvict(value = "posts", key = "#id")
    public void deletePost(String id) {
        postRepository.deleteById(id);
    }
}
