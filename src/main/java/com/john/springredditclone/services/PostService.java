package com.john.springredditclone.services;

import com.john.springredditclone.dto.PostRequest;
import com.john.springredditclone.dto.PostResponse;
import com.john.springredditclone.exceptions.PostNotFoundException;
import com.john.springredditclone.exceptions.SubredditNotFoundException;
import com.john.springredditclone.mappers.PostMapper;
import com.john.springredditclone.models.Post;
import com.john.springredditclone.models.Subreddit;
import com.john.springredditclone.models.User;
import com.john.springredditclone.repositories.PostRepository;
import com.john.springredditclone.repositories.SubredditRepository;
import com.john.springredditclone.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PostService implements IPostService {
    private final SubredditRepository subredditRepository;
    private final AuthService authService;
    private final PostMapper postMapper;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void save(PostRequest postRequest) {
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SubredditNotFoundException(String.format("Subreddit %s was not found", postRequest.getSubredditName())));

        postRepository.save(postMapper.mapToPost(postRequest, subreddit, authService.getCurrentUser()));
    }

    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id.toString()));

        return postMapper.mapToDto(post);
    }

    public List<PostResponse> getAllPosts() {
        return postRepository.findAll().stream().map(postMapper::mapToDto).collect(Collectors.toList());
    }

    public List<PostResponse> getPostsBySubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new SubredditNotFoundException("Subreddit was not found"));

        List<Post> posts = postRepository.findAllBySubreddit(subreddit);

        return posts.stream().map(postMapper::mapToDto).collect(Collectors.toList());
    }

    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("This username does not exist"));

        return postRepository.findByUser(user)
                .stream()
                .map(postMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
