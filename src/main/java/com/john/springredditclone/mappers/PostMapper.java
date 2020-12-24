package com.john.springredditclone.mappers;

import com.john.springredditclone.dto.PostRequest;
import com.john.springredditclone.dto.PostResponse;
import com.john.springredditclone.models.Post;
import com.john.springredditclone.models.Subreddit;
import com.john.springredditclone.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    // These mappings are done automatically if target and expression are the same
    //@Mapping(target = "subreddit", source = "subreddit") // subreddit comes from the method map()
    //@Mapping(target = "user", source = "user") // user comes from the method map()
    @Mapping(target = "description", source = "postRequest.description")
    Post mapToPost(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "id", source = "postId")
    //@Mapping(target = "postName", source = "postName")
    //@Mapping(target = "description", source = "description")
    //@Mapping(target = "url", source = "url")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "userName", source = "user.username")
    PostResponse mapToDto(Post post);

}
