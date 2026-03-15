package com.example.Custom_Authentication.repository;

import com.example.Custom_Authentication.entity.Users;
import com.example.Custom_Authentication.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepo extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String token);
    Optional<VerificationToken> findByUsers(Users users);
}
