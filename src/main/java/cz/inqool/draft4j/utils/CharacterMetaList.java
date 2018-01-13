package cz.inqool.draft4j.utils;

import cz.inqool.draft4j.core.CharacterMetadata;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class CharacterMetaList extends ArrayList<CharacterMetadata> {

    public CharacterMetaList(List<CharacterMetadata> data) {
        this.addAll(data);
    }

    public CharacterMetaList subList(int fromIndex, int toIndex) {
        CharacterMetaList list = new CharacterMetaList();
        list.addAll(super.subList(fromIndex, toIndex));
        return list;
    }

    public CharacterMetaList subList(int fromIndex) {
        CharacterMetaList list = new CharacterMetaList();
        list.addAll(super.subList(fromIndex, this.size()));
        return list;
    }
}
