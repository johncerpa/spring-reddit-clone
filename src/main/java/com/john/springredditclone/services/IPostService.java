package com.john.springredditclone.services;

import com.john.springredditclone.dto.PostRequest;
import com.john.springredditclone.dto.PostResponse;
import com.john.springredditclone.models.Post;

import java.util.List;

public interface IPostService {
    void save(PostRequest postRequest);

    PostResponse getPost(Long id);

    List<PostResponse> getAllPosts();

    List<PostResponse> getPostsBySubreddit(Long id);

    List<PostResponse> getPostsByUsername(String username);
}
