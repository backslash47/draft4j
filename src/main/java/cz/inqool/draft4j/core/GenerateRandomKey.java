package cz.inqool.draft4j.core;

import java.util.HashMap;
import java.util.Map;

public class GenerateRandomKey {
    public static String generateRandomKey() {
        String key = null;
        while (key == null || seenKeys.containsKey(key)) {
            key = Integer.toString((int) Math.floor(Math.random() * MULTIPLIER), 32);
        }
        seenKeys.put(key, true);
        return key;
    }



    private static final Map<String, Boolean> seenKeys = new HashMap<>();
    private static final double MULTIPLIER = Math.pow(2, 24);
}
