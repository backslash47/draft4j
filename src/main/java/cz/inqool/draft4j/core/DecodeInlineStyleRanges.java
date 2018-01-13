package cz.inqool.draft4j.core;

import java.util.List;

import static cz.inqool.draft4j.Utils.fill;
import static cz.inqool.draft4j.Utils.substr;

public class DecodeInlineStyleRanges {
    public static List<DraftInlineStyle> decodeInlineStyleRanges(String text, List<InlineStyleRange> ranges) {

        List<DraftInlineStyle> styles = fill(text.length(), DraftInlineStyle::EMPTY);
        if (ranges != null) {
            ranges.forEach((/*object*/ range) -> {
                int cursor = substr(text, 0, range.getOffset()).length();
                int end = cursor + substr(text, range.getOffset(), range.getLength()).length();
                while (cursor < end) {
                    styles.get(cursor).add(range.getStyle());
                    cursor++;
                }
            });
        }
        return styles;
    }
}
