package cz.inqool.draft4j.core;

import java.util.List;

import static cz.inqool.draft4j.Utils.fill;
import static cz.inqool.draft4j.Utils.substr;


public class DecodeEntityRanges {
    public static List<String> decodeEntityRanges(String text, List<EntityRange> ranges) {
        List<String> entities = fill(text.length(), null);

        if (ranges != null) {
            ranges.forEach(range -> {
                // Using Unicode-enabled substrings converted to JavaScript lengths,
                // fill the output array with entity keys.
                int start = substr(text, 0, range.getOffset()).length();
                int end = start + substr(text, range.getOffset(), range.getLength()).length();
                for (int ii = start; ii < end; ii++) {
                    entities.set(ii, range.getKey());
                }
            });
        }
        return entities;
    }
}
