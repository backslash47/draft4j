package cz.inqool.draft4j.generator;

import cz.inqool.draft4j.core.DraftInlineStyle;

import java.util.function.Function;

public interface InlineStyleFn extends Function<DraftInlineStyle, RenderConfig> {
}
