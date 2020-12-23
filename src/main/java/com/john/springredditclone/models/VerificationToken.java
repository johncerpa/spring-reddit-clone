package com.john.springredditclone.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "token")
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String token;

    private User user;
    private Instant expiryDate;

}
