package com.seai.training_center.attendees.model;

import jakarta.annotation.Nullable;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attendee {

    private UUID id;

    private String name;

    private String email;

    private String telephone;

    private String remark;

    private Boolean isWaiting;

    private UUID courseId;

    private UUID userId;
}
