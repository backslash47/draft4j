package cz.inqool.draft4j.generator;

import cz.inqool.draft4j.core.ContentBlock;

import java.util.function.Function;

public interface BlockStyleFn extends Function<ContentBlock, RenderConfig> {
}
