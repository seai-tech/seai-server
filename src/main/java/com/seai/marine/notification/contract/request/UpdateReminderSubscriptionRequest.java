package com.seai.marine.notification.contract.request;

import com.seai.marine.notification.model.ReminderStatus;
import lombok.Data;

@Data
public class UpdateReminderSubscriptionRequest {
        private ReminderStatus status;
    }

