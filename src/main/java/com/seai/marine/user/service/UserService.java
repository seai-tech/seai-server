package com.seai.marine.user.service;

import com.seai.common.display_id_service.DisplayIdService;
import com.seai.common.exception.ResourceNotFoundException;
import com.seai.marine.document.repository.DocumentRepository;
import com.seai.marine.document.service.DocumentFileService;
import com.seai.marine.email_verification.repository.EmailVerificationRepository;
import com.seai.marine.notification.service.ReminderService;
import com.seai.marine.user.contract.request.UserRegisterRequest;
import com.seai.marine.user.contract.request.UserUpdateRequest;
import com.seai.marine.user.contract.response.CreateUserResponse;
import com.seai.marine.user.contract.response.GetUserResponse;
import com.seai.marine.user.mapper.UserMapper;
import com.seai.marine.user.model.*;
import com.seai.marine.user.repository.UserAuthenticationRepository;
import com.seai.marine.user.repository.UserRepository;
import com.seai.marine.voyage.model.Voyage;
import com.seai.marine.voyage.repository.VoyageRepository;
import com.seai.password_reset.repository.PasswordResetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

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

    private final EmailVerificationRepository emailVerificationRepository;

    private final PasswordResetRepository passwordResetRepository;

    private final DisplayIdService displayIdService;

    @Value("${user.prefix.display-id}")
    private String sailorDisplayIdPrefix;

    @Transactional
    public void delete(UUID uuid) {
        reminderService.turnOffReminderSubscription(uuid);
        passwordResetRepository.deleteUserTokens(uuid);
        emailVerificationRepository.deleteUserToken(uuid);
        voyageRepository.deleteAll(uuid);
        documentRepository.deleteAll(uuid);
        documentFileService.deleteAllForUser(uuid);
        userRepository.delete(uuid);
        userAuthenticationRepository.delete(uuid);
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

    public Optional<GetUserResponse> getUserByDisplayId(String displayId) throws ResourceNotFoundException {
        User user = userRepository.findByDisplayId(displayId)
                .orElseThrow(() -> new ResourceNotFoundException("USER_DISPLAY_ID={" + displayId + "} not found."));
        return Optional.ofNullable(userMapper.mapToGetUserResponse(user));
    }

    @Transactional
    public CreateUserResponse createUser(UserRegisterRequest userRegisterRequest) {
        UUID userId = UUID.randomUUID();
        User user = userMapper.map(userRegisterRequest);
        user.setId(userId);
        user.setDisplayId(displayIdService.generateDisplayId(sailorDisplayIdPrefix, null));
        userAuthService.save(userRegisterRequest, userId);
        userRepository.save(user);
        reminderService.turnOnReminderSubscription(userId, userRegisterRequest.getEmail());
        return new CreateUserResponse("Account created successfully, to complete your registration, please confirm your email address", user);
    }

    public List<Experience> getUserExperience(UUID userId) {
        List<Voyage> voyages = voyageRepository.findByUserId(userId);
        voyages.sort(Comparator.comparing(voyage -> voyage.getJoiningDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
        List<Experience> experiences = new ArrayList<>();
        int totalDaysOnBoard = 0;
        int totalDaysOnShore = 0;
        Map<VesselType, Integer> vesselTypeDuration = new HashMap<>();
        Map<Rank, Integer> rankDuration = new HashMap<>();
        LocalDate previousLeavingDate = null;

        for (Voyage voyage : voyages) {
            Rank rank = voyage.getRank();
            VesselType vesselType = voyage.getVesselType();
            LocalDate joiningDate = voyage.getJoiningDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate leavingDate = voyage.getLeavingDate() != null ?
                    voyage.getLeavingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() :
                    LocalDate.now();
            int daysOnBoard = (int) ChronoUnit.DAYS.between(joiningDate, leavingDate);
            totalDaysOnBoard += daysOnBoard;

            rankDuration.put(rank, rankDuration.getOrDefault(rank, 0) + daysOnBoard);
            vesselTypeDuration.put(vesselType, vesselTypeDuration.getOrDefault(vesselType, 0) + daysOnBoard);

            if (previousLeavingDate != null) {
                int daysOnShore = (int) ChronoUnit.DAYS.between(previousLeavingDate, joiningDate);
                totalDaysOnShore += daysOnShore;
            }
            previousLeavingDate = leavingDate;
        }
        for (Map.Entry<VesselType, Integer> entry : vesselTypeDuration.entrySet()) {
            experiences.add(new Experience("Time on vessel type " + entry.getKey().toString(),
                    formatDuration(entry.getValue())));
        }
        for (Map.Entry<Rank, Integer> entry : rankDuration.entrySet()) {
            experiences.add(new Experience("Time on rank " + entry.getKey(), formatDuration(entry.getValue())));
        }
        experiences.add(new Experience("Total time on board", formatDuration(totalDaysOnBoard)));
        experiences.add(new Experience("Total time on shore", formatDuration(totalDaysOnShore)));

        return experiences;
    }

    public String formatDuration(int totalDays) {
        int years = totalDays / 365;
        int remainingDays = totalDays % 365;
        int months = (int) Math.floor(remainingDays / 30.4375);
        int days = remainingDays - (months * 30);
        return years + " years, " + months + " months, " + days + " days";
    }
}

