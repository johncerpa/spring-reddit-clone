package com.john.springredditclone.controllers;

import com.john.springredditclone.dto.PostRequest;
import com.john.springredditclone.dto.PostResponse;
import com.john.springredditclone.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity createPost(@RequestBody PostRequest postRequest) {
        postService.save(postRequest);

        return new ResponseEntity(CREATED);
    }

    @GetMapping("/{id}")
    public PostResponse getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    @GetMapping("/")
    public List<PostResponse> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/by-subreddit/{id}")
    public List<PostResponse> getPostsBySubreddit(@PathVariable Long id) {
        return postService.getPostsBySubreddit(id);
    }

    @GetMapping("/by-user/{username}")
    public List<PostResponse> getPostsByUsername(@PathVariable String username) {
        return postService.getPostsByUsername(username);
    }


}
