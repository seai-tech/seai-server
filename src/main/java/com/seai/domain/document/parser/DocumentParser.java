package com.seai.domain.document.parser;

import com.seai.domain.document.model.MarineDocument;

import java.util.List;

public interface DocumentParser {

    boolean canParseDocument(List<String> lines);

    MarineDocument parseDocument(List<String> lines);
}
