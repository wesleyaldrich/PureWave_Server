package com.purewave.service;

import com.purewave.model.Post;
import com.purewave.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    // Fetch only primary posts (attachedTo == null)
    public List<Post> getPrimaryPosts() {
        return postRepository.findByAttachedToIsNull();
    }

    // Fetch replies for a given post ID
    public List<Post> getRepliesByPostId(String postId) {
        return postRepository.findByAttachedTo(postId);
    }

    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    public void deletePost(String id) {
        postRepository.deleteById(id);
    }
}
