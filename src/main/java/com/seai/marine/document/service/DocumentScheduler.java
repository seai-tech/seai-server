package com.seai.marine.document.service;

import com.seai.marine.document.contract.response.GetDocumentsForReminderResponse;
import com.seai.marine.document.repository.DocumentRepository;
import com.seai.marine.notification.email.EmailSender;
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

    @Scheduled(cron = "40 15 11 * * ?")
    public void checkCertificatesDaily() {
        LocalDate today = LocalDate.now();
        List<GetDocumentsForReminderResponse> documents = documentRepository.findDocumentsForUsersWithReminders();
        for (GetDocumentsForReminderResponse document : documents) {
            LocalDate expiryDate = document.getExpiryDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate sixtyDaysBeforeExp = expiryDate.minusDays(60);
            if (today.equals(sixtyDaysBeforeExp)) {
                emailService.sendSimpleMessage(document.getUserEmail(),
                        "Reminder: Certificate expires in 60 days",
                        "Your certificate is nearing expiration in 60 days:" + " " + document.getName() + "Expiring on:" + " "  + document.getExpiryDate() );
            } else if (today.equals(expiryDate)) {
                emailService.sendSimpleMessage(document.getUserEmail(),
                        "Notice: Certificate has expired",
                        "Your certificate has expired:" + " " +document.getName());
            }
        }
    }
}
