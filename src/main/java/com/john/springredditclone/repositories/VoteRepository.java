package com.john.springredditclone.repositories;

import com.john.springredditclone.models.Post;
import com.john.springredditclone.models.User;
import com.john.springredditclone.models.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
