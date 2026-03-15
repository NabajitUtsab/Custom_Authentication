package com.example.Custom_Authentication.security;

import com.example.Custom_Authentication.entity.Users;
import com.example.Custom_Authentication.repository.UsersRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepo usersRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users users = usersRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));

        return User.builder().
                username(users.getEmail()).
                password(users.getPassword()).
                authorities(List.of(new SimpleGrantedAuthority("USER"))).
                build();
    }
}
