package com.example.Custom_Authentication.service;

import com.example.Custom_Authentication.dto.LoginRequest;
import com.example.Custom_Authentication.dto.RegisterRequest;
import com.example.Custom_Authentication.entity.Users;
import com.example.Custom_Authentication.entity.VerificationToken;
import com.example.Custom_Authentication.repository.UsersRepo;
import com.example.Custom_Authentication.repository.VerificationTokenRepo;
import com.example.Custom_Authentication.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsersRepo usersRepo;
    private final VerificationTokenRepo verificationTokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;


    public void registerUser(RegisterRequest registerRequest) {

        if (usersRepo.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        Users users = Users.builder().
                email(registerRequest.getEmail()).
                password(passwordEncoder.encode(registerRequest.getPassword())).
                verified(false).
                build();

        usersRepo.save(users);
        sendVerification(users);
    }



    private void sendVerification(Users users) {
        Optional<VerificationToken> existing = verificationTokenRepo.findByUsers(users);

        if(existing.isPresent()){
            LocalDateTime existingTokenTime = existing.get().getCreateDate().plusMinutes(5);

            if(existingTokenTime.isAfter(LocalDateTime.now())){
                throw new RuntimeException("Email already sent. Try after 5 minutes");
            }

            else{
                verificationTokenRepo.delete(existing.get());
            }
        }

        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = VerificationToken.builder().
                token(token).
                createDate(LocalDateTime.now()).
                expiryDate(LocalDateTime.now().plusMinutes(10)).
                users(users).
                build();

        verificationTokenRepo.save(verificationToken);

        String link = "http://localhost:8080/api/auth/verify?token="+token;

        emailService.sendVerificationEmail(users.getEmail(),link);
    }


    public void verifyUser(String token) {

        VerificationToken existingToken = verificationTokenRepo.findByToken(token).orElseThrow();

        LocalDateTime existingTokenTime = existingToken.getCreateDate().plusMinutes(5);

        if(existingTokenTime.isBefore(LocalDateTime.now())){
            throw new RuntimeException("Token Expired");
        }

        Users users = existingToken.getUsers();

        users.setVerified(true);

        usersRepo.save(users);

        verificationTokenRepo.delete(existingToken);
    }


    public String loginUser(LoginRequest loginRequest) {
        Users users = usersRepo.findByEmail(loginRequest.getEmail()).orElseThrow();

        if (!passwordEncoder.matches(loginRequest.getPassword(), users.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        if(users.getVerified()==false){

            sendVerification(users);
            throw new RuntimeException("User is not verified.Link sent ");
        }

        return jwtUtil.generateJwtToken(users.getEmail());
    }
}
