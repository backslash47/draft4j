package cz.inqool.draft4j.core;

import cz.inqool.draft4j.utils.CharacterMetaList;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static cz.inqool.draft4j.Utils.fill;

@Getter
@Setter
public class ContentBlock {
    private String key;
    private String text;
    private int depth;
    private String type;
    private CharacterMetaList characterList;

    private List<EntityRange> entityRanges;
    private List<InlineStyleRange> inlineStyleRanges;

    public ContentBlock(BlockNodeConfig config) {
        config = decorateCharacterList(config);

        this.key = config.getKey();
        this.text = config.getText();
        this.depth = config.getDepth();
        this.type = config.getType();
        this.characterList = config.getCharacterList();
    }

    private BlockNodeConfig decorateCharacterList(BlockNodeConfig config) {
        if (config == null) {
            return null;
        }

        CharacterMetaList characterList = config.getCharacterList();
        String text = config.getText();

        if (text != null && characterList == null) {
            config.setCharacterList(new CharacterMetaList(fill(text.length(), CharacterMetadata::EMPTY)));
        }

        return config;
    }
}
