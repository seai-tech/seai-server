package com.seai.marine.document.parser;

import com.seai.marine.document.model.MarineDocument;

import java.util.List;

public interface DocumentParser {

    boolean canParseDocument(List<String> lines);

    MarineDocument parseDocument(List<String> lines);
}
