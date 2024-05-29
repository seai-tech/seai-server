package com.seai.marine.user.Service;

import com.seai.marine.document.repository.DocumentRepository;
import com.seai.marine.document.service.DocumentFileService;
import com.seai.marine.notification.service.ReminderService;
import com.seai.marine.user.contract.request.UserRegisterRequest;
import com.seai.marine.user.contract.request.UserUpdateRequest;
import com.seai.marine.user.contract.response.GetUserResponse;
import com.seai.marine.user.mapper.UserMapper;
import com.seai.marine.user.model.User;
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

    private final DocumentFileService documentFileService;

    private final UserMapper userMapper;

    private final UserAuthService userAuthService;

    private final ReminderService reminderService;

    public void delete(UUID uuid) {
        voyageRepository.deleteAll(uuid);
        documentRepository.deleteAll(uuid);
        userRepository.delete(uuid);
        userAuthenticationRepository.delete(uuid);
        documentFileService.deleteAllForUser(uuid);
    }

    public void updateUser(UserUpdateRequest updateUserRequest, UUID userId) {
        userRepository.findById(userId);
        User user = userMapper.map(updateUserRequest);
        userRepository.update(userId, user);
    }

    public GetUserResponse getUserById(UUID userId) {
        User user = userRepository.findById(userId);
        return userMapper.mapToGetUserResponse(user);
    }

    public void createUser(UserRegisterRequest userRegisterRequest) {
        UUID id = UUID.randomUUID();
        userAuthService.save(userRegisterRequest, id);
        User user = userMapper.map(userRegisterRequest);
        userRepository.save(user, id);
        reminderService.turnOnReminderSubscription(id, userRegisterRequest.getEmail());
    }

}

