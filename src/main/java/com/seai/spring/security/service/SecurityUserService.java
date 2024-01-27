package com.seai.spring.security.service;

import com.seai.spring.security.model.SecurityUser;
import com.seai.user.model.User;
import com.seai.user.model.UserAuthentication;
import com.seai.user.repository.UserAuthenticationRepository;
import com.seai.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
@RequiredArgsConstructor
public class SecurityUserService implements UserDetailsService {

    private final UserAuthenticationRepository userAuthenticationRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        UserAuthentication user = userAuthenticationRepository.findByEmail(userId);
        return new SecurityUser(user.getId(), user.getEmail(), user.getPassword(), Collections.emptyList());
    }
}
