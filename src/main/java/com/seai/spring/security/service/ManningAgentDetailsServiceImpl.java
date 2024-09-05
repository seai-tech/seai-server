package com.seai.spring.security.service;

import com.seai.common.exception.ResourceNotFoundException;
import com.seai.manning_agent.manning_agent.model.ManningAgentAuthentication;
import com.seai.manning_agent.manning_agent.repository.ManningAgentAuthRepository;
import com.seai.spring.security.model.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ManningAgentDetailsServiceImpl implements UserDetailsService {

    private final ManningAgentAuthRepository manningAgentAuthRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<ManningAgentAuthentication> manningAgent = Optional.ofNullable(manningAgentAuthRepository.findByEmail(email).orElseThrow(() ->
                    new ResourceNotFoundException("User with email not found : " + email)));
        ManningAgentAuthentication manningAgentAuth = manningAgent.get();
        return new SecurityUser(manningAgentAuth.getId(), manningAgentAuth.getEmail(), manningAgentAuth.getPassword()
                , Collections.emptyList());
    }
}
