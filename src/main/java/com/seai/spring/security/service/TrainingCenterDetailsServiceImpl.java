package com.seai.spring.security.service;

import com.seai.marine.user.model.UserAuthentication;
import com.seai.spring.security.model.SecurityUser;
import com.seai.training_center.training_center.repository.TrainingCenterAuthenticationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
@RequiredArgsConstructor
public class TrainingCenterDetailsServiceImpl implements UserDetailsService {

    private final TrainingCenterAuthenticationRepository trainingCenterAuthenticationRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        UserAuthentication user = trainingCenterAuthenticationRepository.findByEmail(userId);
        return new SecurityUser(user.getId(), user.getEmail(), user.getPassword(), Collections.emptyList());
    }
}
