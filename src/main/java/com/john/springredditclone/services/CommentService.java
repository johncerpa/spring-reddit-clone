package com.john.springredditclone.services;

import com.john.springredditclone.dto.CommentDto;
import com.john.springredditclone.exceptions.PostNotFoundException;
import com.john.springredditclone.mappers.CommentMapper;
import com.john.springredditclone.models.Comment;
import com.john.springredditclone.models.NotificationEmail;
import com.john.springredditclone.models.Post;
import com.john.springredditclone.models.User;
import com.john.springredditclone.repositories.CommentRepository;
import com.john.springredditclone.repositories.PostRepository;
import com.john.springredditclone.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;

    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;
    private static final String POST_URL = "ui/posts/postId";

    public void save(CommentDto commentDto) {
        Post post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post was not found"));

        Comment comment = commentMapper.map(commentDto, post, authService.getCurrentUser());

        commentRepository.save(comment);

        String message = mailContentBuilder.build(
                String.format("%s posted a comment on your post: %s", authService.getCurrentUser(), POST_URL)
        );

        sendCommentNotification(message, post.getUser());
    }

    public void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail(
                String.format("%s commented on your post", user.getUsername()),
                user.getEmail(),
                message
        ));
    }

    public List<CommentDto> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post not found"));

        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CommentDto> getAllCommentsForUser(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("This user was not found"));

        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
