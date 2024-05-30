package com.seai.marine.document.service;

import com.seai.marine.document.model.MarineDocumentWithEmail;
import com.seai.marine.document.repository.DocumentRepository;
import com.seai.marine.notification.email.EmailSender;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@AllArgsConstructor
public class DocumentScheduler {
    private final DocumentRepository documentRepository;
    private final EmailSender emailService;
    @Scheduled(cron = "${document.scheduler.cron}")
    public void checkCertificatesDaily() {
        LocalDate today = LocalDate.now();
        List<MarineDocumentWithEmail> documents = documentRepository.findDocumentsForUsersWithReminders();
        for (MarineDocumentWithEmail document : documents) {
            LocalDate expiryDate = document.getMarineDocument().getExpiryDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (today.equals(expiryDate)) {
                emailService.sendSimpleMessage(document.getEmail(),
                        "Notice: Certificate has expired",
                        "Your certificate has expired:" + " " + document.getMarineDocument().getName());
            } else {
                long monthsUntilExpiration = ChronoUnit.MONTHS.between(today.withDayOfMonth(1), expiryDate.withDayOfMonth(1));
                emailService.sendSimpleMessage(document.getEmail(),
                        "Reminder: Certificate is expiring",
                        "Your certificate :" + document.getMarineDocument().getName() + " is expiring in " + monthsUntilExpiration + " months.");
            }
        }
    }
}
