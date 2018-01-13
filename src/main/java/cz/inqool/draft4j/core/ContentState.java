package cz.inqool.draft4j.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
public class ContentState {
    private BlockMap blockMap;
    private Map<String, String> entityMap;

    public List<ContentBlock> getBlocksAsArray() {
        return new ArrayList<>(blockMap.values());
    }

    public DraftEntityInstance getEntity(String key) {
        return DraftEntity.__get(key);
    }
}
