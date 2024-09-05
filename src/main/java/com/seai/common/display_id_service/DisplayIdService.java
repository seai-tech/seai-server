package com.seai.common.display_id_service;

import com.seai.manning_agent.manning_agent.repository.ManningAgentRepository;
import com.seai.manning_agent.sailor.repository.ManningAgentSailorRepository;
import com.seai.marine.user.repository.UserRepository;
import com.seai.training_center.training_center.repository.TrainingCenterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DisplayIdService {

    private final UserRepository userRepository;

    private final TrainingCenterRepository trainingCenterRepository;

    private final ManningAgentRepository manningAgentRepository;

    private final ManningAgentSailorRepository manningAgentSailorRepository;

    public String generateDisplayId(String prefix, UUID manningAgentId) {
        int randomNumber = new Random().nextInt(9000000) + 1000000;
        String displayId = prefix + randomNumber;

        if (Objects.equals(prefix, "S")) {
            if (manningAgentId != null) {
                if (manningAgentSailorRepository.findSailorByDisplayId(manningAgentId, displayId).isPresent()) {
                    return generateDisplayId(prefix, manningAgentId);
                }
            } else {
                if (userRepository.findByDisplayId(displayId).isPresent()) {
                    return generateDisplayId(prefix, null);
                }
            }
        } else if (Objects.equals(prefix, "T")) {
            if (trainingCenterRepository.findTrainingCenterByDisplayId(displayId).isPresent()) {
                return generateDisplayId(prefix, null);
            }
        } else if (Objects.equals(prefix, "M")) {
            if (manningAgentRepository.findByDisplayId(displayId).isPresent()) {
                return generateDisplayId(prefix, null);
            }
        }
        return displayId;
    }
}
