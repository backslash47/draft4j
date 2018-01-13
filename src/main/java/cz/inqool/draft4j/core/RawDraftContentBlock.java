package cz.inqool.draft4j.core;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class RawDraftContentBlock {
    private String key;
    private String type;
    private String text;
    private int depth;
    private List<InlineStyleRange> inlineStyleRanges;
    private List<RawEntityRange> entityRanges;
    private Map data;
}
