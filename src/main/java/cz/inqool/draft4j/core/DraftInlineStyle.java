package cz.inqool.draft4j.core;

import java.util.LinkedHashSet;

public class DraftInlineStyle extends LinkedHashSet<String> {
    public static DraftInlineStyle EMPTY() {
        return new DraftInlineStyle();
    }
}
