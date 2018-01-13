package cz.inqool.draft4j.helpers;

import cz.inqool.draft4j.generator.StyleMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class OrderedStyleMap {
    private StyleMap styleMap;
    private StyleOrder styleOrder;
}
