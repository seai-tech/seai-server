package com.seai.manning_agent.sailor.document.parser;

import com.seai.manning_agent.sailor.document.model.MarineDocument;

import java.util.List;

public interface DocumentParser {

    boolean canParseDocument(List<String> lines);

    MarineDocument parseDocument(List<String> lines);
}
