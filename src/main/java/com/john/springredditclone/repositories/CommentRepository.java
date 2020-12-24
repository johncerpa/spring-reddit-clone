package com.john.springredditclone.repositories;

import com.john.springredditclone.models.Comment;
import com.john.springredditclone.models.Post;
import com.john.springredditclone.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}
