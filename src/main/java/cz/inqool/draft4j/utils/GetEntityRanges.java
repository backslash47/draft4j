package cz.inqool.draft4j.utils;

import cz.inqool.draft4j.core.CharacterMetadata;
import cz.inqool.draft4j.core.DraftInlineStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GetEntityRanges {
    public static List<EntityRange> getEntityRanges(String text, CharacterMetaList charMetaList) {
        String charEntity = null;
        String prevCharEntity = null;
        List<EntityRange> ranges = new ArrayList<>();
        int rangeStart = 0;
        for (int i = 0, len = text.length(); i < len; i++) {
            prevCharEntity = charEntity;
            CharacterMetadata meta = charMetaList.get(i);
            charEntity = meta != null ? meta.getEntity() : null;
            if (i > 0 && !Objects.equals(charEntity, prevCharEntity)) {
                ranges.add(new EntityRange(prevCharEntity,
                        getStyleRanges(text.substring(rangeStart, i), charMetaList.subList(rangeStart, i))
                ));

                rangeStart = i;
            }
        }
        ranges.add(new EntityRange(charEntity,
                getStyleRanges(text.substring(rangeStart), charMetaList.subList(rangeStart))
        ));
        return ranges;
    }

    private static List<StyleRange> getStyleRanges(String text, CharacterMetaList charMetaList) {
        DraftInlineStyle charStyle = EMPTY_SET();
        DraftInlineStyle prevCharStyle = EMPTY_SET();
        List<StyleRange> ranges = new ArrayList<>();
        int rangeStart = 0;
        for (int i = 0, len = text.length(); i < len; i++) {
            prevCharStyle = charStyle;
            CharacterMetadata meta = charMetaList.get(i);
            charStyle = meta != null ? meta.getStyle() : EMPTY_SET();
            if (i > 0 && !Objects.equals(charStyle, prevCharStyle)) {
                ranges.add(new StyleRange(text.substring(rangeStart, i), prevCharStyle));
                rangeStart = i;
            }
        }
        ranges.add(new StyleRange(text.substring(rangeStart), charStyle));
        return ranges;
    }

    private static final DraftInlineStyle EMPTY_SET() {
        return new DraftInlineStyle();
    }
}
