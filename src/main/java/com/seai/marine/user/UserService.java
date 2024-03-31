package com.seai.marine.user;

import com.seai.marine.document.repository.DocumentRepository;
import com.seai.marine.user.repository.UserAuthenticationRepository;
import com.seai.marine.user.repository.UserRepository;
import com.seai.marine.voyage.repository.VoyageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final DocumentRepository documentRepository;

    private final VoyageRepository voyageRepository;

    private final UserAuthenticationRepository userAuthenticationRepository;

    public void delete(UUID uuid) {
        voyageRepository.deleteAll(uuid);
        documentRepository.deleteAll(uuid);
        userRepository.delete(uuid);
        userAuthenticationRepository.delete(uuid);
    }
}
