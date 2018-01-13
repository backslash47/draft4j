package cz.inqool.draft4j.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CharacterMetadata {
    private DraftInlineStyle style = new DraftInlineStyle();
    private String entity;

    public static CharacterMetadata EMPTY() {
        return new CharacterMetadata();
    }
}
