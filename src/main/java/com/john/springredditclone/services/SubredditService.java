package com.john.springredditclone.services;

import com.john.springredditclone.dto.SubredditDto;
import com.john.springredditclone.exceptions.SpringRedditException;
import com.john.springredditclone.mappers.SubredditMapper;
import com.john.springredditclone.models.Subreddit;
import com.john.springredditclone.repositories.SubredditRepository;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubredditService implements ISubredditService {

    private final SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;

    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {
        Subreddit savedSubreddit = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));

        subredditDto.setId(savedSubreddit.getId());

        return subredditDto;
    }

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll().stream().map(subredditMapper::mapSubredditToDto).collect(Collectors.toList());
    }

    public SubredditDto getSubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new SpringRedditException(String.format("Subreddit with id %d not found", id)));

        return subredditMapper.mapSubredditToDto(subreddit);
    }
}
