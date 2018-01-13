package cz.inqool.draft4j.generator;

import java.util.HashMap;

public class StyleMap extends HashMap<String, RenderConfig> {
    public StyleMap copy() {
        StyleMap map = new StyleMap();
        map.putAll(this);
        return map;
    }
}
