package cz.inqool.draft4j.generator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Options {
    private StyleMap inlineStyles;
    private InlineStyleFn inlineStyleFn;
    private BlockRendererMap blockRenderers;
    private BlockStyleFn blockStyleFn;
    private EntityStyleFn entityStyleFn;
    private String defaultBlockTag = "undefined";       // needed to simulate javascript undefined vs null behaviour
}
