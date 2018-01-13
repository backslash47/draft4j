package cz.inqool.draft4j.core;

import java.util.LinkedHashMap;
import java.util.Map;

public class BlockMap extends LinkedHashMap<String, ContentBlock> {
    public BlockMap(Map<String, ContentBlock> data) {
        this.putAll(data);
    }
}
