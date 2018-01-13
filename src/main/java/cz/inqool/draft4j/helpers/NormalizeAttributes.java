package cz.inqool.draft4j.helpers;

import cz.inqool.draft4j.generator.Attributes;

import java.util.HashMap;
import java.util.Map;

public class NormalizeAttributes {
    public static Attributes normalizeAttributes(Attributes attributes) {
        if (attributes == null) {
            return null;
        }
        Attributes normalized = new Attributes();
        boolean didNormalize = false;
        for (String name : attributes.keySet()) {
            String newName = name;
            if (ATTR_NAME_MAP.containsKey(name)) {
                newName = ATTR_NAME_MAP.get(name);
                didNormalize = true;
            }
            normalized.put(newName, attributes.get(name));
        }
        return didNormalize ? normalized : attributes;
    }

    static {{
        ATTR_NAME_MAP = new HashMap<String, String>() {{
            put("acceptCharset", "accept-charset");
            put("className", "class");
            put("htmlFor", "for");
            put("httpEquiv", "http-equiv");
        }};
    }}

    private static final Map<String, String> ATTR_NAME_MAP;
}
