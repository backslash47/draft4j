package cz.inqool.draft4j.utils;

import cz.inqool.draft4j.core.DraftInlineStyle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class StyleRange {
    private String text;
    private DraftInlineStyle styleSet;
}
