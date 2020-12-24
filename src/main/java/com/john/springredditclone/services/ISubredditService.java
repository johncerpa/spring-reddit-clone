package com.john.springredditclone.services;

import com.john.springredditclone.dto.SubredditDto;
import java.util.List;

public interface ISubredditService {
    SubredditDto save(SubredditDto subredditDto);

    List<SubredditDto> getAll();

    SubredditDto getSubreddit(Long id);
}
