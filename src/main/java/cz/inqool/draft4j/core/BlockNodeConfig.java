package cz.inqool.draft4j.core;

import cz.inqool.draft4j.utils.CharacterMetaList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BlockNodeConfig {
    private CharacterMetaList characterList;
    private Map data;
    private int depth;
    private String key;
    private String text;
    private String type;
}
