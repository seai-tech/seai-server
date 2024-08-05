package com.seai.marine.user.service;

import com.seai.marine.document.repository.DocumentRepository;
import com.seai.marine.document.service.DocumentFileService;
import com.seai.marine.email_verification.repository.EmailVerificationRepository;
import com.seai.marine.notification.service.ReminderService;
import com.seai.marine.user.contract.request.UserRegisterRequest;
import com.seai.marine.user.contract.request.UserUpdateRequest;
import com.seai.marine.user.contract.response.CreateUserResponse;
import com.seai.marine.user.contract.response.GetUserResponse;
import com.seai.marine.user.mapper.UserMapper;
import com.seai.marine.user.model.Experience;
import com.seai.marine.user.model.Rank;
import com.seai.marine.user.model.User;
import com.seai.marine.user.model.VesselType;
import com.seai.marine.user.repository.UserAuthenticationRepository;
import com.seai.marine.user.repository.UserRepository;
import com.seai.marine.voyage.model.Voyage;
import com.seai.marine.voyage.repository.VoyageRepository;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public void delete(UUID uuid) {
        reminderService.turnOffReminderSubscription(uuid);
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

    public CreateUserResponse createUser(UserRegisterRequest userRegisterRequest) {
        UUID id = UUID.randomUUID();
        userAuthService.save(userRegisterRequest, id);
        User user = userMapper.map(userRegisterRequest);
        userRepository.save(user, id);
        user.setId(id);
        reminderService.turnOnReminderSubscription(id, userRegisterRequest.getEmail());
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

