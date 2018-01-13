package cz.inqool.draft4j.helpers;

import cz.inqool.draft4j.generator.RenderConfig;
import cz.inqool.draft4j.generator.StyleMap;

public class CombineOrderedStyles {
    public static OrderedStyleMap combineOrderedStyles(StyleMap customMap, OrderedStyleMap defaults) {
        if (customMap == null) {
            return defaults;
        }

        StyleMap defaultStyleMap = defaults.getStyleMap();
        StyleOrder defaultStyleOrder = defaults.getStyleOrder();

        StyleMap styleMap = defaultStyleMap.copy();
        StyleOrder styleOrder = defaultStyleOrder.copy();

        for (String styleName : customMap.keySet()) {
            if (defaultStyleMap.containsKey(styleName)) {
                RenderConfig defaultStyles = defaultStyleMap.get(styleName);
                styleMap.put(styleName, RenderConfig.merge(defaultStyles, customMap.get(styleName)));
            } else {
                styleMap.put(styleName, customMap.get(styleName));
                styleOrder.add(styleName);
            }
        }
        return new OrderedStyleMap(styleMap, styleOrder);
    }
}
