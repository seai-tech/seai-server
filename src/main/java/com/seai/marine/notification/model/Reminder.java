package com.seai.marine.notification.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Data
@AllArgsConstructor
@Getter
@Setter
public class Reminder {
    private UUID id;
    private UUID userId;
    private String email;
}
