package com.john.springredditclone.mappers;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.john.springredditclone.dto.PostRequest;
import com.john.springredditclone.dto.PostResponse;
import com.john.springredditclone.models.Post;
import com.john.springredditclone.models.Subreddit;
import com.john.springredditclone.models.User;
import com.john.springredditclone.repositories.CommentRepository;
import com.john.springredditclone.repositories.VoteRepository;
import com.john.springredditclone.services.IAuthService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private IAuthService authService;

    // These mappings are done automatically if target and expression are the same
    //@Mapping(target = "subreddit", source = "subreddit") // subreddit comes from the method map()
    //@Mapping(target = "user", source = "user") // user comes from the method map()
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "voteCount", constant = "0")
    public abstract Post mapToPost(PostRequest postRequest, Subreddit subreddit, User user);


    //@Mapping(target = "postName", source = "postName")
    //@Mapping(target = "description", source = "description")
    //@Mapping(target = "url", source = "url")
    @Mapping(target = "id", source = "postId")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    public abstract PostResponse mapToDto(Post post);

    Integer commentCount(Post post) {
        return commentRepository.findByPost(post).size();
    }

    String getDuration(Post post) {
        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }

}
