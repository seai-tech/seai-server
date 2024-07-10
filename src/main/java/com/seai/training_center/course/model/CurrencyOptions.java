package com.seai.training_center.course.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum CurrencyOptions {

    USD("USD"),
    EUR("EUR"),
    GBP("GBP"),
    BGN("BGN");

    private final String displayName;

    CurrencyOptions(String displayName) {
        this.displayName = displayName;
    }

    @JsonCreator
    public static CurrencyOptions fromName(String name) {
        return Arrays.stream(values())
                .filter(f -> f.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
