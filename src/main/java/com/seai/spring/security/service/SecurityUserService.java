package com.seai.spring.security.service;

import com.seai.spring.security.model.SecurityUser;
import com.seai.user.model.SeaiUser;
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

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        SeaiUser seaiUser = userRepository.findByEmail(userId);
        return new SecurityUser(seaiUser.getId(),seaiUser.getEmail(), seaiUser.getPassword(), Collections.emptyList());
    }
}
