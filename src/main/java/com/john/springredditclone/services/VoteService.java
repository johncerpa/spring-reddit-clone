package com.john.springredditclone.services;

import com.john.springredditclone.dto.VoteDto;
import com.john.springredditclone.exceptions.PostNotFoundException;
import com.john.springredditclone.exceptions.SpringRedditException;
import com.john.springredditclone.models.Post;
import com.john.springredditclone.models.Vote;
import com.john.springredditclone.models.VoteType;
import com.john.springredditclone.repositories.PostRepository;
import com.john.springredditclone.repositories.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoteService implements IVoteService {
    private final PostRepository postRepository;
    private final VoteRepository voteRepository;
    private final IAuthService authService;

    @Transactional
    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                                    .orElseThrow(() -> new PostNotFoundException("Post was not found"));

        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());

        boolean areVoteTypesEqual = voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType());

        if (voteByPostAndUser.isPresent() && areVoteTypesEqual) {
            throw new SpringRedditException("You have already done this action");
        }

        if (VoteType.UPVOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }

        voteRepository.save(mapToVote(voteDto, post));

        postRepository.save(post);
    }

    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}
