package cz.inqool.draft4j.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class EntityRange {
    private String entityKey;
    private List<StyleRange> stylePieces;
}
