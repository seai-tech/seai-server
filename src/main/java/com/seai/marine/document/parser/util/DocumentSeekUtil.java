package com.seai.marine.document.parser.util;

import com.seai.exception.ReadDocumentException;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.function.Predicate;

@UtilityClass
public class DocumentSeekUtil {

    public static String findMatchFor(Predicate<String> predicate, List<String> words, int occurrence) {
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
