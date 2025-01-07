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
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserProfileService userProfileService;

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
    public Post savePost(String id, @RequestBody Post post, Authentication authentication) {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");
        String picture = oauthUser.getAttribute("picture");

        // Cache the user's profile image using a UserProfileService
        String cachedProfileImageUrl = userProfileService.getOrSaveUserProfile(email, picture);

        System.out.println(cachedProfileImageUrl);

        post.setUserId(email);
        post.setName(name);
        post.setPicture(cachedProfileImageUrl);

        post.setAttachedTo(id);
        // update the replyCount of parent
        if (id != null) {
            Optional<Post> optionalPost = postRepository.findById(id);
            if (optionalPost.isPresent()) {
                Post parentPost = optionalPost.get();

                // Update parent and cache
                updateRepliedPost(parentPost);
            }
            else {
                throw new IllegalArgumentException("Post not found with ID: " + id);
            }
        }

        post.setReplyCount(0);

        return postRepository.save(post);
    }

    @CachePut(value = "posts", key = "#repliedPost.id")
    public Post updateRepliedPost(Post repliedPost) {
        repliedPost.setReplyCount((repliedPost.getReplyCount() == null ? 1 : repliedPost.getReplyCount()) + 1);
        return postRepository.save(repliedPost);
    }

    @CachePut(value = "posts", key = "#id")
    public Post editPost(String id, Post updatedPost, Authentication authentication) {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");

        Post existingPost = postRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + id));

        // Check if it's the right user
        if (!existingPost.getUserId().equals(email)) {
            throw new SecurityException("You are not authorized to edit this post.");
        }

        existingPost.setContent(updatedPost.getContent());
        existingPost.setAttachment(updatedPost.getAttachment());

        return postRepository.save(existingPost);
    }

    @CacheEvict(value = "posts", key = "#id")
    public void deletePost(String id, Authentication authentication) {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");

        Post existingPost = postRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + id));

        // Check if it's the right user
        if (!existingPost.getUserId().equals(email)) {
            throw new SecurityException("You are not authorized to delete this post.");
        }

        postRepository.deleteById(id);
    }
}
