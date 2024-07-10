package com.seai.marine.notification.scheduler;

import com.seai.marine.document.model.MarineDocumentWithEmail;
import com.seai.marine.document.repository.DocumentRepository;
import com.seai.marine.notification.email.EmailSender;
import com.seai.marine.user.contract.response.GetUserResponse;
import com.seai.marine.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@ConditionalOnProperty(prefix = "document.scheduler", name = "enabled", havingValue = "true")
@Slf4j
public class NotificationScheduler {
    private final DocumentRepository documentRepository;
    private final EmailSender emailService;
    private final UserService userService;

    @Scheduled(cron = "${document.scheduler.cron}")
    public void sendNotifications() {
        log.debug("Sending notifications job run");
        LocalDate today = LocalDate.now();
        List<MarineDocumentWithEmail> documents = documentRepository.findDocumentsForUsersWithReminders();
        log.debug("Sending notifications for {} documents", documents.size());

        Map<String, List<MarineDocumentWithEmail>> documentsByEmail = documents.stream()
                .collect(Collectors.groupingBy(MarineDocumentWithEmail::getEmail));

        for (Map.Entry<String, List<MarineDocumentWithEmail>> entry : documentsByEmail.entrySet()) {
            String email = entry.getKey();
            List<MarineDocumentWithEmail> userDocuments = entry.getValue();
            GetUserResponse user = userService.getUserById(userDocuments.get(0).getMarineDocument().getUserId());
            String name = user.getFirstName() + " " + user.getLastName();
            List<MarineDocumentWithEmail> expiringDocuments = new ArrayList<>();
            List<MarineDocumentWithEmail> expiredDocuments = new ArrayList<>();

            for (MarineDocumentWithEmail document : userDocuments) {
                LocalDate expiryDate = document.getMarineDocument().getExpiryDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if (today.equals(expiryDate)) {
                    expiredDocuments.add(document);
                } else {
                    expiringDocuments.add(document);
                }
            }

            if (!expiringDocuments.isEmpty()) {
                String expiringEmailContent = generateEmailContent(name, expiringDocuments, "set to expire soon");
                emailService.sendSimpleMessage(email, "Reminder: Certificates are expiring", expiringEmailContent);
            }

            if (!expiredDocuments.isEmpty()) {
                String expiredEmailContent = generateEmailContent(name, expiredDocuments, "have expired");
                emailService.sendSimpleMessage(email, "Notice: Certificates have expired", expiredEmailContent);
            }
        }
    }

    private String generateEmailContent(String name, List<MarineDocumentWithEmail> documents, String status) {
        StringBuilder content = new StringBuilder();
        content.append("Dear ").append(name).append(",\n\n");
        content.append("We are writing to inform you that the following documents ").append(status).append(":\n\n");

        for (MarineDocumentWithEmail document : documents) {
            String expiryDate = document.getMarineDocument().getExpiryDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString();
            content.append(String.format("%s - Expires on %s\n", document.getMarineDocument().getName(), expiryDate));
        }

        content.append("\nAs a valued member of our community, we want to ensure that you have ample time to renew your documents and remain compliant.\n");
        content.append("To facilitate this process, you can easily book a slot to renew your documents via our Training Center tab on the SeAI platform.\n");
        content.append("\nIf you have any questions or need further assistance, please do not hesitate to contact us.\n\n");
        content.append("Best regards,\n\n");
        content.append("The SeAI Team");

        return content.toString();
    }
}
