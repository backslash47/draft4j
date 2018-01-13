package cz.inqool.draft4j.generator;

import java.util.HashMap;

public class Attributes extends HashMap<String, String> {
    public static Attributes singleton(String key, String value) {
        Attributes attributes = new Attributes();
        attributes.put(key, value);

        return attributes;
    }

    public Attributes invariantAdd(String key, String value) {
        Attributes attributes = new Attributes();
        attributes.putAll(this);
        attributes.put(key, value);
        return attributes;
    }
}
