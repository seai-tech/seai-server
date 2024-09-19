package com.seai.manning_agent.sailor.document.parser.util;

import com.seai.exception.ReadDocumentException;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@UtilityClass
public class DocumentSeekUtil {

    public static String findNthMatch(Predicate<String> predicate, List<String> words, int occurrence) {
        int occur = 0;
        for (String word : words) {
            if (predicate.test(word)) {
                occur++;
                if (occur == occurrence) {
                    return word;
                }
            }
        }
        throw new ReadDocumentException("Could not extract document data");
    }

    public static String findNthMatchForReversed(Predicate<String> predicate, List<String> words, int occurrence) {
        int occur = 0;
        List<String> reversed = new ArrayList<>(words);
        Collections.reverse(reversed);
        for (String word : reversed) {
            if (predicate.test(word)) {
                occur++;
                if (occur == occurrence) {
                    return word;
                }
            }
        }
        throw new ReadDocumentException("Could not extract document data");
    }

    public static String getWordAfter(Predicate<String> predicate, List<String> words) {
        for (int i = 0; i < words.size(); i++) {
            String word = words.get(i);
            if (predicate.test(word)) {
                return words.get(i + 1);
            }
        }
        throw new ReadDocumentException("Could not extract document data");
    }
}
