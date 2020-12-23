package com.john.springredditclone.services;

import com.john.springredditclone.dto.RegisterRequest;
import com.john.springredditclone.models.NotificationEmail;
import com.john.springredditclone.models.User;
import com.john.springredditclone.models.VerificationToken;
import com.john.springredditclone.repositories.UserRepository;
import com.john.springredditclone.repositories.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;

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

}
