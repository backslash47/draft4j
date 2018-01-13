package cz.inqool.draft4j.helpers;

import cz.inqool.draft4j.generator.StyleDescr;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class StyleToCSS {
    public static String styleToCSS(StyleDescr styleDescr) {
            return styleDescr.keySet().stream()
                    .map(name -> {
                        String styleValue = processStyleValue(name, styleDescr.get(name));
                        String styleName = processStyleName(name);
                        return styleName + ": " + styleValue;
                    })
                    .collect(Collectors.joining("; "));
    }

    private static String processStyleName(String name) {
        return name
                .replaceAll(UPPERCASE_PATTERN, "-$1")
                .toLowerCase()
                .replace(VENDOR_PREFIX, "-$1-");
    }

    private static String processStyleValue(String name, Object value) {
        boolean isNumeric;
        if (value instanceof String) {
            isNumeric = ((String) value).matches(NUMERIC_STRING);
        } else {
            isNumeric = true;
            value = String.valueOf(value);
        }
        if (!isNumeric || value.equals("0") || isUnitlessNumber.get(name) == Boolean.TRUE) {
            return (String)value;
        } else {
            return value + "px";
        }
    }
    
    static {{
        isUnitlessNumber = new HashMap<String, Boolean>() {{
            put("animationIterationCount", true);
            put("borderImageOutset", true);
            put("borderImageSlice", true);
            put("borderImageWidth", true);
            put("boxFlex", true);
            put("boxFlexGroup", true);
            put("boxOrdinalGroup", true);
            put("columnCount", true);
            put("flex", true);
            put("flexGrow", true);
            put("flexPositive", true);
            put("flexShrink", true);
            put("flexNegative", true);
            put("flexOrder", true);
            put("gridRow", true);
            put("gridRowEnd", true);
            put("gridRowSpan", true);
            put("gridRowStart", true);
            put("gridColumn", true);
            put("gridColumnEnd", true);
            put("gridColumnSpan", true);
            put("gridColumnStart", true);
            put("fontWeight", true);
            put("lineClamp", true);
            put("lineHeight", true);
            put("opacity", true);
            put("order", true);
            put("orphans", true);
            put("tabSize", true);
            put("widows", true);
            put("zIndex", true);
            put("zoom", true);
                    // SVG-related properties
            put("fillOpacity", true);
            put("floodOpacity", true);
            put("stopOpacity", true);
            put("strokeDasharray", true);
            put("strokeDashoffset", true);
            put("strokeMiterlimit", true);
            put("strokeOpacity", true);
            put("strokeWidth", true);
        }};
    }}

    private static final Map<String, Boolean> isUnitlessNumber;

    private static final String VENDOR_PREFIX = "^(moz|ms|o|webkit)-";
    private static final String NUMERIC_STRING = "^\\d+$";
    private static final String UPPERCASE_PATTERN = "([A-Z])";
}
