package com.seai.manning_agent.sailor.document.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
@AllArgsConstructor
public class MarineDocumentWithEmail {
    private MarineDocument marineDocument;
    private String email;
}
