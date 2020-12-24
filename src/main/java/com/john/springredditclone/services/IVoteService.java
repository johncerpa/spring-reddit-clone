package com.john.springredditclone.services;

import com.john.springredditclone.dto.VoteDto;

public interface IVoteService {
    void vote(VoteDto voteDto);
}
