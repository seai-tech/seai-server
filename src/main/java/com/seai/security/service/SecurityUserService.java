package com.seai.security.service;

import com.seai.domain.user.model.SeaiUser;
import com.seai.domain.user.repository.UserRepository;
import com.seai.security.model.SecurityUser;
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
        SeaiUser seaiUser = userRepository.find(userId);
        return new SecurityUser(seaiUser.getId(),seaiUser.getEmail(), seaiUser.getPassword(), Collections.emptyList());
    }
}
