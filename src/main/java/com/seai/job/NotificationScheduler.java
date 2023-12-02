package com.seai.job;

import com.seai.domain.document.repository.DocumentRepository;
import com.seai.domain.notification.model.DocumentNotification;
import com.seai.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class NotificationScheduler {

    private final NotificationRepository notificationRepository;

    private final DocumentRepository documentRepository;

    @Scheduled(cron = "@hourly")
    public void notifyPeople() {
        List<DocumentNotification> currentNotifications = notificationRepository.getCurrentNotifications(Instant.now(), new Date());
        documentRepository.findByIds(currentNotifications.stream().map(DocumentNotification::getDocumentId).toList());
    }
}
