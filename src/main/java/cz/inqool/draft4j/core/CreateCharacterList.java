package cz.inqool.draft4j.core;

import cz.inqool.draft4j.utils.CharacterMetaList;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CreateCharacterList {
    public static CharacterMetaList createCharacterList(List<DraftInlineStyle> inlineStyles, List<String> entities) {

        List<CharacterMetadata> characterArray = IntStream.range(0, inlineStyles.size()).mapToObj(ii -> {
            DraftInlineStyle style = inlineStyles.get(ii);
            String entity = entities.get(ii);

            return new CharacterMetadata(style, entity);
        }).collect(Collectors.toList());

        return new CharacterMetaList(characterArray);
    }
}
