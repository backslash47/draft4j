package cz.inqool.draft4j.generator;

import cz.inqool.draft4j.core.DraftEntityInstance;

import java.util.function.Function;

public interface EntityStyleFn extends Function<DraftEntityInstance, RenderConfig> {
}
