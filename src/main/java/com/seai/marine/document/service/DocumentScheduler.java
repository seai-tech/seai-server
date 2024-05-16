package com.seai.marine.document.service;

import com.seai.marine.document.model.MarineDocument;
import com.seai.marine.document.repository.DocumentRepository;
import com.seai.marine.notification.email.EmailSender;
import com.seai.marine.user.model.User;
import com.seai.marine.user.model.UserAuthentication;
import com.seai.marine.user.repository.UserAuthenticationRepository;
import com.seai.marine.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
@AllArgsConstructor
public class DocumentScheduler {
    private final DocumentRepository documentRepository;
    private final EmailSender emailService;
    private final UserAuthenticationRepository userAuthenticationRepository;
        private final UserRepository userRepository;

    @Scheduled(cron = "40 47 11 * * ?")
    public void checkCertificatesDaily() {
        LocalDate today = LocalDate.now();
        List<UserAuthentication> userAuthenticationUsers = userAuthenticationRepository.findAllUsers();
        for (UserAuthentication user : userAuthenticationUsers) {
            User user_details = userRepository.findById(user.getId());
            if (!user_details.getReminder_subscription()) {
                continue;
            }
            List<MarineDocument> documents = documentRepository.findDocumentsByEmail(user.getEmail());

            for (MarineDocument document : documents) {
                LocalDate expiryDate = document.getExpiryDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate oneMonthBeforeExp = expiryDate.minusMonths(1);

                if (today.isEqual(oneMonthBeforeExp) || (today.isAfter(oneMonthBeforeExp) && today.isBefore(expiryDate))) {
                    emailService.sendSimpleMessage(user.getEmail(), "Reminder: Certificate expires in one month or less", "Your certificate is nearing expiration.");
                } else if (today.isAfter(expiryDate) || today.equals(expiryDate)) {
                    emailService.sendSimpleMessage(user.getEmail(), "Notice: Certificate has expired", "Your certificate has expired.");
                }
            }
        }
    }
}
