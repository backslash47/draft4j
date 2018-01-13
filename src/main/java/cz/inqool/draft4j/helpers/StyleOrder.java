package cz.inqool.draft4j.helpers;

import java.util.ArrayList;

public class StyleOrder extends ArrayList<String> {
    public StyleOrder copy() {
        StyleOrder order = new StyleOrder();
        order.addAll(this);
        return order;
    }
}
