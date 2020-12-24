package com.john.springredditclone.repositories;

import com.john.springredditclone.models.Post;
import com.john.springredditclone.models.Subreddit;
import com.john.springredditclone.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // Pretty cool feature, remember this!!
    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);
}
