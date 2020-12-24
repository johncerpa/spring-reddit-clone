package com.john.springredditclone.services;

import com.john.springredditclone.dto.CommentDto;
import com.john.springredditclone.models.User;

import java.util.List;

public interface ICommentService {
    void save(CommentDto commentDto);

    List<CommentDto> getAllCommentsForPost(Long postId);

    void sendCommentNotification(String message, User user);
}
