package com.seai.marine.user.Service;

import com.seai.marine.user.contract.request.UserRegisterRequest;
import com.seai.marine.user.mapper.UserAuthenticationMapper;
import com.seai.marine.user.model.UserAuthentication;
import com.seai.marine.user.repository.UserAuthenticationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final UserAuthenticationRepository userAuthenticationRepository;
    private final UserAuthenticationMapper userAuthenticationMapper;


    public void save(UserRegisterRequest userRegisterRequest, UUID id) {
       UserAuthentication userAuthentication = userAuthenticationMapper.map(userRegisterRequest);
        userAuthenticationRepository.save(userAuthentication, id);
    }


}