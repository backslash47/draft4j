package cz.inqool.draft4j.core;

import java.util.HashMap;
import java.util.Map;

public class DraftEntity {
    private static final Map<String, DraftEntityInstance> instances = new HashMap<>();
    private static int instanceKey = 0;

    /**
     * Retrieve the entity corresponding to the supplied key string.
     */
    public static DraftEntityInstance __get(String key) {
        return instances.get(key);
    }

    /**
     * Create a DraftEntityInstance and store it for later retrieval.
     *
     * A random key string will be generated and returned. This key may
     * be used to track the entity's usage in a ContentBlock, and for
     * retrieving data about the entity at render time.
     */
    public static String __create(String type, String mutability, Map<String, String> data) {
        return __add(new DraftEntityInstance(type, mutability, data != null ? data : new HashMap<>()));
    }

    /**
     * Add an existing DraftEntityInstance to the DraftEntity map. This is
     * useful when restoring instances from the server.
     */
    public static String __add(DraftEntityInstance instance) {
        String key = "" + ++instanceKey;
        instances.put(key, instance);
        return key;
    }
}
