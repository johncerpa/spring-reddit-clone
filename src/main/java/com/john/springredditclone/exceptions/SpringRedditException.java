package com.john.springredditclone.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class SpringRedditException extends RuntimeException {
    public SpringRedditException(String message, Exception e) {
        super(message);
    }

    public SpringRedditException(String message) {
        super(message);
    }
}

// Note: we can also use an ExceptionControllerHandler

/*
      @ResponseStatus(value=HttpStatus.CONFLICT, reason="Data integrity violation")
      @ExceptionHandler(DataIntegrityViolationException.class)
      public void conflict() {
        // Nothing to do
      }
 */