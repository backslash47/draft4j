package cz.inqool.draft4j;

import cz.inqool.draft4j.core.ContentBlock;
import cz.inqool.draft4j.core.ContentState;
import cz.inqool.draft4j.core.DraftEntityInstance;
import cz.inqool.draft4j.core.DraftInlineStyle;
import cz.inqool.draft4j.generator.*;
import cz.inqool.draft4j.helpers.OrderedStyleMap;
import cz.inqool.draft4j.helpers.StyleOrder;
import cz.inqool.draft4j.utils.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static cz.inqool.draft4j.generator.Attributes.singleton;
import static cz.inqool.draft4j.Utils.join;
import static cz.inqool.draft4j.Utils.repeat;
import static cz.inqool.draft4j.helpers.CombineOrderedStyles.combineOrderedStyles;
import static cz.inqool.draft4j.helpers.NormalizeAttributes.normalizeAttributes;
import static cz.inqool.draft4j.helpers.StyleToCSS.styleToCSS;
import static cz.inqool.draft4j.utils.GetEntityRanges.getEntityRanges;
import static cz.inqool.draft4j.utils.INLINE_STYLE.CODE;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@Getter
@Setter
public class MarkupGenerator {

    private static final String INDENT = "  ";
    private static final String BREAK = "<br></br>";
    private static final String DATA_ATTRIBUTE = "^data-([a-z0-9-]+)$";

    private static final StyleMap DEFAULT_STYLE_MAP = new StyleMap() {{
        put(INLINE_STYLE.BOLD, new RenderConfig("strong"));
        put(INLINE_STYLE.CODE, new RenderConfig("code"));
        put(INLINE_STYLE.ITALIC, new RenderConfig("em"));
        put(INLINE_STYLE.STRIKETHROUGH, new RenderConfig("del"));
        put(INLINE_STYLE.UNDERLINE, new RenderConfig("u"));
    }};

    private static final StyleOrder DEFAULT_STYLE_ORDER = new StyleOrder() {{
        add(INLINE_STYLE.BOLD);
        add(INLINE_STYLE.ITALIC);
        add(INLINE_STYLE.UNDERLINE);
        add(INLINE_STYLE.STRIKETHROUGH);
        add(INLINE_STYLE.CODE);
    }};

    private static final Map<String, AttrMap> ENTITY_ATTR_MAP = new HashMap<String, AttrMap>() {{
        put(ENTITY_TYPE.LINK, new AttrMap() {{
            put("url", "href");
            put("href", "href");
            put("rel", "rel");
            put("target", "target");
            put("title", "title");
            put("className", "class");
        }});

        put(ENTITY_TYPE.IMAGE, new AttrMap() {{
            put("src", "src");
            put("height", "height");
            put("width", "width");
            put("alt", "alt");
            put("className", "class");
        }});
    }};

    private static final Map<String, BiFunction<String, DraftEntityInstance, Attributes>> DATA_TO_ATTR = new HashMap<String, BiFunction<String, DraftEntityInstance, Attributes>>() {{
        put(ENTITY_TYPE.LINK, (entityType, entity) -> {
            AttrMap attrMap = ENTITY_ATTR_MAP.containsKey(entityType) ? ENTITY_ATTR_MAP.get(entityType) : new AttrMap();
            Map<String, String> data = entity.getData();
            Attributes attrs = new Attributes();
            for (String dataKey : data.keySet()) {
                String dataValue = data.get(dataKey);
                if (attrMap.containsKey(dataKey)) {
                    String attrKey = attrMap.get(dataKey);
                    attrs.put(attrKey, dataValue);
                } else if (dataKey.matches(DATA_ATTRIBUTE)) {
                    attrs.put(dataKey, dataValue);
                }
            }
            return attrs;
        });

        put(ENTITY_TYPE.IMAGE, (entityType, entity) -> {
            AttrMap attrMap = ENTITY_ATTR_MAP.containsKey(entityType) ? ENTITY_ATTR_MAP.get(entityType) : new AttrMap();
            Map<String, String> data = entity.getData();
            Attributes attrs = new Attributes();
            for (String dataKey : data.keySet()) {
                String dataValue = data.get(dataKey);
                if (attrMap.containsKey(dataKey)) {
                    String attrKey = attrMap.get(dataKey);
                    attrs.put(attrKey, dataValue);
                } else if (dataKey.matches(DATA_ATTRIBUTE)) {
                    attrs.put(dataKey, dataValue);
                }
            }
            return attrs;
        });
    }};

    // The reason this returns an array is because a single block might get wrapped
    // in two tags.
    private static List<String> getTags(String blockType, String defaultBlockTag) {
        switch (blockType) {
            case BLOCK_TYPE.HEADER_ONE:
                return singletonList("h1");
            case BLOCK_TYPE.HEADER_TWO:
                return singletonList("h2");
            case BLOCK_TYPE.HEADER_THREE:
                return singletonList("h3");
            case BLOCK_TYPE.HEADER_FOUR:
                return singletonList("h4");
            case BLOCK_TYPE.HEADER_FIVE:
                return singletonList("h5");
            case BLOCK_TYPE.HEADER_SIX:
                return singletonList("h6");
            case BLOCK_TYPE.UNORDERED_LIST_ITEM:
            case BLOCK_TYPE.ORDERED_LIST_ITEM:
                return singletonList("li");
            case BLOCK_TYPE.BLOCKQUOTE:
                return singletonList("blockquote");
            case BLOCK_TYPE.CODE:
                return asList("pre", "code");
            case BLOCK_TYPE.ATOMIC:
                return singletonList("figure");
            default:
                if (defaultBlockTag == null) {
                    return emptyList();
                }
                return singletonList(!defaultBlockTag.equals("undefined") ? defaultBlockTag : "p");
        }
    }

    private static String getWrapperTag(String blockType) {
        switch (blockType) {
            case BLOCK_TYPE.UNORDERED_LIST_ITEM:
                return "ul";
            case BLOCK_TYPE.ORDERED_LIST_ITEM:
                return "ol";
            default:
                return null;
        }
    }

    // These are related to state.
    private List<ContentBlock> blocks;
    private ContentState contentState;
    private int currentBlock;
    private int indentLevel;
    private List<String> output;
    private int totalBlocks;
    private String wrapperTag;

    // These are related to user-defined options.
    private Options options;
    private StyleMap inlineStyles;
    private InlineStyleFn inlineStyleFn;
    private StyleOrder styleOrder;

    MarkupGenerator(ContentState contentState, Options options) {
        if (options == null) {
            options = new Options();
        }

        this.contentState = contentState;
        this.options = options;

        OrderedStyleMap orderedStyleMap = combineOrderedStyles(options.getInlineStyles(),
                new OrderedStyleMap(DEFAULT_STYLE_MAP, DEFAULT_STYLE_ORDER)
        );

        StyleMap inlineStyles = orderedStyleMap.getStyleMap();
        StyleOrder styleOrder = orderedStyleMap.getStyleOrder();

        this.inlineStyles = inlineStyles;
        this.inlineStyleFn = options.getInlineStyleFn();
        this.styleOrder = styleOrder;
    }

    String generate() {
        this.output = new ArrayList<>();
        this.blocks = this.contentState.getBlocksAsArray();
        this.totalBlocks = this.blocks.size();
        this.currentBlock = 0;
        this.indentLevel = 0;
        this.wrapperTag = null;
        while (this.currentBlock < this.totalBlocks) {
            this.processBlock();
        }
        this.closeWrapperTag();
        return join(this.output, "").trim();
    }

    private void processBlock() {
        BlockRendererMap blockRenderers = this.options.getBlockRenderers();
        String defaultBlockTag = this.options.getDefaultBlockTag();

        ContentBlock block = this.blocks.get(this.currentBlock);
        String blockType = block.getType();
        String newWrapperTag = getWrapperTag(blockType);

        if (!Objects.equals(this.wrapperTag, newWrapperTag)) {
            if (this.wrapperTag != null) {
                this.closeWrapperTag();
            }
            if (newWrapperTag != null) {
                this.openWrapperTag(newWrapperTag);
            }
        }
        this.indent();
        // Allow blocks to be rendered using a custom renderer.
        BlockRenderer customRenderer =
                blockRenderers != null && blockRenderers.containsKey(blockType)
                        ? blockRenderers.get(blockType)
                        : null;
        String customRendererOutput = customRenderer != null ? customRenderer.apply(block) : null;
        // Renderer can return null, which will cause processing to continue as normal.
        if (customRendererOutput != null) {
            this.output.add(customRendererOutput);
            this.output.add("\n");
            this.currentBlock += 1;
            return;
        }
        this.writeStartTag(block, defaultBlockTag);
        this.output.add(this.renderBlockContent(block));

        // Look ahead and see if we will nest list.
        ContentBlock nextBlock = this.getNextBlock();
        if (canHaveDepth(blockType) && nextBlock != null && nextBlock.getDepth() == block.getDepth() + 1) {
            this.output.add("\n");
            // This is a litle hacky: temporarily stash our current wrapperTag and
            // render child list(s).
            String thisWrapperTag = this.wrapperTag;
            this.wrapperTag = null;
            this.indentLevel += 1;
            this.currentBlock += 1;
            this.processBlocksAtDepth(nextBlock.getDepth());
            this.wrapperTag = thisWrapperTag;
            this.indentLevel -= 1;
            this.indent();
        } else {
            this.currentBlock += 1;
        }
        this.writeEndTag(block, defaultBlockTag);
    }

    private void processBlocksAtDepth(int depth) {
        ContentBlock block = this.blocks.get(this.currentBlock);
        while (block != null && block.getDepth() == depth) {
            this.processBlock();
            block = this.blocks.get(this.currentBlock);
        }
        this.closeWrapperTag();
    }

    private ContentBlock getNextBlock() {
        if (currentBlock + 1 < totalBlocks) {
            return this.blocks.get(this.currentBlock + 1);
        } else {
            return null;
        }
    }

    private void writeStartTag(ContentBlock block, String defaultBlockTag) {
        List<String> tags = getTags(block.getType(), defaultBlockTag);

        String attrString;
        if (this.options.getBlockStyleFn() != null) {
            RenderConfig renderConfig = this.options.getBlockStyleFn().apply(block);
            if (renderConfig == null) {
                renderConfig = new RenderConfig();
            }

            Attributes attributes = renderConfig.getAttributes();
            StyleDescr style = renderConfig.getStyle();

            // Normalize `className` -> `class`, etc.
            attributes = normalizeAttributes(attributes);
            if (style != null) {
                String styleAttr = styleToCSS(style);
                attributes = attributes == null ? singleton("style", styleAttr) : attributes.invariantAdd("style", styleAttr);
            }
            attrString = stringifyAttrs(attributes);
        } else {
            attrString = "";
        }

        for (String tag : tags) {
            this.output.add("<"+tag+attrString+">");
        }
    }

    private void writeEndTag(ContentBlock block, String defaultBlockTag) {
        List<String> tags = getTags(block.getType(), defaultBlockTag);
        if (tags.size() == 1) {
            this.output.add("</"+tags.get(0)+">\n");
        } else {
            List<String> output = new ArrayList<>();
            for (String tag : tags) {
                output.add(0, "</"+tag+">");
            }
            this.output.add(join(output, "") + '\n');
        }
    }

    private void openWrapperTag(String wrapperTag) {
        this.wrapperTag = wrapperTag;
        this.indent();
        this.output.add("<"+wrapperTag+">\n");
        this.indentLevel += 1;
    }

    private void closeWrapperTag() {
        if (wrapperTag != null) {
            this.indentLevel -= 1;
            this.indent();
            this.output.add("</"+wrapperTag+">\n");
            this.wrapperTag = null;
        }
    }

    private void indent() {
        this.output.add(repeat(INDENT, this.indentLevel));
    }

    private String withCustomInlineStyles(String content, DraftInlineStyle styleSet) {
        if (this.inlineStyleFn == null) {
            return content;
        }

        RenderConfig renderConfig = this.inlineStyleFn.apply(styleSet);
        if (renderConfig == null) {
            return content;
        }

        String element = renderConfig.getElement();
        if (element == null) {
            element = "span";
        }

        Attributes attributes = renderConfig.getAttributes();
        StyleDescr style = renderConfig.getStyle();

        String attrString = stringifyAttrs(attributes.invariantAdd("style", style != null ? styleToCSS(style) : null));

        return "<"+element+attrString+">"+content+"</"+element+">";
    }

    private String renderBlockContent(ContentBlock block) {
        String blockType = block.getType();
        String text = block.getText();
        if (Objects.equals(text, "")) {
            // Prevent element collapse if completely empty.
            return BREAK;
        }
        text = this.preserveWhitespace(text);
        CharacterMetaList charMetaList = block.getCharacterList();
        List<EntityRange> entityPieces = getEntityRanges(text, charMetaList);

        return entityPieces.stream().map(range -> {
            String entityKey = range.getEntityKey();
            List<StyleRange> stylePieces = range.getStylePieces();

            String content = stylePieces.stream().map(styleRange -> {
                String rangeText = styleRange.getText();
                DraftInlineStyle styleSet = styleRange.getStyleSet();

                String rangeContent = encodeContent(rangeText);
                for (String styleName : this.styleOrder) {
                    // If our block type is CODE then don't wrap inline code elements.
                    if (Objects.equals(styleName, CODE) && Objects.equals(blockType, BLOCK_TYPE.CODE)) {
                        continue;
                    }
                    if (styleSet.contains(styleName)) {
                        RenderConfig renderConfig = this.inlineStyles.get(styleName);
                        String element = renderConfig.getElement();
                        Attributes attributes = renderConfig.getAttributes();
                        StyleDescr style = renderConfig.getStyle();

                        if (element == null) {
                            element = "span";
                        }
                        // Normalize `className` -> `class`, etc.
                        attributes = normalizeAttributes(attributes);
                        if (style != null) {
                            String styleAttr = styleToCSS(style);
                            attributes = attributes == null ? Attributes.singleton("style", styleAttr) : attributes.invariantAdd("style", styleAttr);
                        }
                        String attrString = stringifyAttrs(attributes);
                        rangeContent = "<"+element+attrString+">"+rangeContent+"</"+element+">";
                    }
                }

                return this.withCustomInlineStyles(rangeContent, styleSet);
            }).collect(Collectors.joining(""));

            DraftEntityInstance entity = entityKey != null ? this.contentState.getEntity(entityKey) : null;
            // Note: The `toUpperCase` below is for compatability with some libraries that use lower-case for image blocks.
            String entityType = entity == null ? null : entity.getType().toUpperCase();
            RenderConfig entityStyle;
            if (entity != null && this.options.getEntityStyleFn() != null &&
                    (entityStyle = this.options.getEntityStyleFn().apply(entity)) != null) {

                String element = entityStyle.getElement();
                Attributes attributes = entityStyle.getAttributes();
                StyleDescr style = entityStyle.getStyle();

                if (element == null) {
                    element = "span";
                }
                // Normalize `className` -> `class`, etc.
                attributes = normalizeAttributes(attributes);
                if (style != null) {
                    String styleAttr = styleToCSS(style);
                    attributes = attributes == null ? Attributes.singleton("style", styleAttr) : attributes.invariantAdd("style", styleAttr);
                }
                String attrString = stringifyAttrs(attributes);
                return "<"+element+attrString+">"+content+"</"+element+">";
            } else if (entityType != null && entityType.equals(ENTITY_TYPE.LINK)) {
                Attributes attrs = DATA_TO_ATTR.containsKey(entityType) ? DATA_TO_ATTR.get(entityType).apply(entityType, entity) : null;

                String attrString = stringifyAttrs(attrs);
                return "<a"+attrString+">"+content+"</a>";
            } else if (entityType != null && entityType.equals(ENTITY_TYPE.IMAGE)) {
                Attributes attrs = DATA_TO_ATTR.containsKey(entityType) ? DATA_TO_ATTR.get(entityType).apply(entityType, entity) : null;

                String attrString = stringifyAttrs(attrs);
                return "<img"+attrString+"/>";
            } else {
                return content;
            }
        }).collect(Collectors.joining(""));
    }

    private String preserveWhitespace(String text) {
        int length = text.length();
        // Prevent leading/trailing/consecutive whitespace collapse.
        List<Character> newText = Arrays.asList(new Character[length]);

        for (int i = 0; i < length; i++) {
            if (text.charAt(i) == ' ' && (i == 0 || i == length - 1 || text.charAt(i - 1) == ' ')) {
                newText.set(i, '\u00A0');
            } else {
                newText.set(i, text.charAt(i));
            }
        }

        return newText.stream().map(Object::toString).collect(Collectors.joining());
    }

    private String stringifyAttrs(Attributes attrs) {
        if (attrs == null) {
            return "";
        }
        List<String> parts = new ArrayList<>();
        for (String name : attrs.keySet()) {
            String value = attrs.get(name);
            if (value != null) {
                parts.add(" "+name+"=\""+encodeAttr(value + "")+"\"");
            }
        }
        return join(parts, "");
    }

    private boolean canHaveDepth(String blockType) {
        switch (blockType) {
            case BLOCK_TYPE.UNORDERED_LIST_ITEM:
            case BLOCK_TYPE.ORDERED_LIST_ITEM:
                return true;
            default:
                return false;
        }
    }

    private String encodeContent(String text) {
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\u00A0", "&#160;")
                .replace("\n", BREAK + "\n");
    }

    private String encodeAttr(String text) {
        return text.replace("&", "&amp;")
                .replace("<","&lt;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
