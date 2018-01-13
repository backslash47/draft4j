package cz.inqool.draft4j.generator;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
public class RenderConfig {
    private String element;
    private Attributes attributes;
    private StyleDescr style;

    public RenderConfig(String element) {
        this.element = element;
    }

    public static RenderConfig merge(RenderConfig first, RenderConfig second) {
        RenderConfig config = new RenderConfig();
        config.setElement(first.getElement() != null ? first.getElement() : second.getElement());
        config.setAttributes(first.getAttributes() != null ? first.getAttributes() : second.getAttributes());
        config.setStyle(first.getStyle() != null ? first.getStyle() : second.getStyle());

        return config;
    }
}
