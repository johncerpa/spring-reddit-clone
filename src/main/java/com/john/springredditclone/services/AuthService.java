package com.john.springredditclone.services;

import com.john.springredditclone.dto.AuthenticationResponse;
import com.john.springredditclone.dto.LoginRequest;
import com.john.springredditclone.dto.RegisterRequest;
import com.john.springredditclone.exceptions.SpringRedditException;
import com.john.springredditclone.models.NotificationEmail;
import com.john.springredditclone.models.User;
import com.john.springredditclone.models.VerificationToken;
import com.john.springredditclone.repositories.UserRepository;
import com.john.springredditclone.repositories.VerificationTokenRepository;
import com.john.springredditclone.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Value("${application.host}")
    private String host;

    @Value("${server.port}")
    private String port;

    @Transactional
    public void signUp(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setCreatedAt(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);

        String token = generateVerificationToken(user);

        String url = String.format("http://%s:%s/api/auth/accountVerification/%s", host, port, token);

        mailService.sendMail(
                new NotificationEmail(
                        "Activate your account, please!",
                        user.getEmail(),
                        "Click the URL to activate your account " + url
                ));
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);

        return token;
    }

    @Override
    public void verifyAccount(String token) {
        Optional<VerificationToken> optToken = verificationTokenRepository.findByToken(token);

        optToken.orElseThrow(() -> new SpringRedditException("Invalid token"));

        fetchUserAndEnable(optToken.get());
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();

        // We could improve this by throwing custom exceptions in the service
        // and creating ExceptionControllerHandlers with custom http status codes and messages

        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new SpringRedditException(String.format("User %s was not found", username)));

        // A managed hibernate entity
        user.setEnabled(true);

        // Hibernate flush!
        userRepository.save(user);
    }

    @Override
    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        String token = jwtProvider.generateToken(authenticate);

        return new AuthenticationResponse(token, loginRequest.getUsername());
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }
}
