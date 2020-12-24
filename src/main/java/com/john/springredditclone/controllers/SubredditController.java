package com.john.springredditclone.controllers;

import com.john.springredditclone.dto.SubredditDto;
import com.john.springredditclone.services.SubredditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/subreddits")
@RequiredArgsConstructor
@Slf4j
public class SubredditController {
    private final SubredditService subredditService;

    @PostMapping
    public ResponseEntity<SubredditDto> createSubreddit(@RequestBody SubredditDto subredditDto) {
        SubredditDto savedDto = subredditService.save(subredditDto);
        return ResponseEntity.status(CREATED).body(savedDto);
    }

    @GetMapping
    public ResponseEntity<List<SubredditDto>> getAllSubreddits() {
        return ResponseEntity.status(OK).body(subredditService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubredditDto> getSubreddit(@PathVariable Long id) {
        SubredditDto subredditDto = subredditService.getSubreddit(id);

        return ResponseEntity.status(OK).body(subredditDto);
    }
}
