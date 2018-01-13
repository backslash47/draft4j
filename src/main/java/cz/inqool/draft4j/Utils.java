package cz.inqool.draft4j;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public class Utils {
    public static String repeat(String str, int times) {
        return Stream.generate(() -> str).limit(times).collect(joining());
    }

    public static String join(List<String> strs, String delimiter) {
        return strs.stream().collect(Collectors.joining(delimiter));
    }

    public static <T> List<T> fill(int n, Supplier<T> o) {
        if (n < 0)
            throw new IllegalArgumentException("List length = " + n);

        List<T> list = new ArrayList<>();
        IntStream.range(0, n).forEach(i -> list.add(o != null ? o.get() : null));

        return list;
    }

    public static String substr(String text, int offset, int length) {
        return text.substring(offset, offset + length);
    }
}
