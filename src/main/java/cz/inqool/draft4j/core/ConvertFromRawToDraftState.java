package cz.inqool.draft4j.core;

import cz.inqool.draft4j.utils.CharacterMetaList;

import java.util.*;
import java.util.stream.Collectors;

import static cz.inqool.draft4j.core.CreateCharacterList.createCharacterList;
import static cz.inqool.draft4j.core.DecodeEntityRanges.decodeEntityRanges;
import static cz.inqool.draft4j.core.DecodeInlineStyleRanges.decodeInlineStyleRanges;
import static cz.inqool.draft4j.core.GenerateRandomKey.generateRandomKey;
import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toList;

public class ConvertFromRawToDraftState {
    public static ContentState convertFromRawToDraftState(RawDraftContentState rawState) {

        // decode entities
        Map<String, String> entityMap = decodeRawEntityMap(rawState);

        // decode blockMap
        BlockMap blockMap = decodeRawBlocks(rawState, entityMap);

        return new ContentState(blockMap, entityMap);
    }

    private static Map<String, String> decodeRawEntityMap(RawDraftContentState rawState) {
        Map<String, RawDraftEntity> rawEntityMap = rawState.getEntityMap();

        Map<String, String> entityMap = new HashMap<>();

        // TODO: Update this once we completely remove DraftEntity
        rawEntityMap.keySet().forEach(rawEntityKey -> {
            RawDraftEntity rawDraftEntity = rawEntityMap.get(rawEntityKey);

            String type = rawDraftEntity.getType();
            String mutability = rawDraftEntity.getMutability();
            Map<String, String> data = rawDraftEntity.getData();

            // get the key reference to created entity
            entityMap.put(rawEntityKey, DraftEntity.__create(type, mutability, data != null ? data : emptyMap()));
          });

        return entityMap;
    };

    private static BlockMap decodeRawBlocks(RawDraftContentState rawState, Map<String, String> entityMap) {
        List<RawDraftContentBlock> rawBlocks = rawState.getBlocks();

        return decodeContentBlocks(rawBlocks, entityMap);
    }

    private static BlockMap decodeContentBlocks(List<RawDraftContentBlock> blocks, Map<String, String> entityMap) {
        return new BlockMap(blocks.stream()
                .map(block -> new ContentBlock(decodeBlockNodeConfig(block, entityMap)))
                .collect(Collectors.toMap(ContentBlock::getKey, c -> c, (a,b) -> null, LinkedHashMap::new))
        );
    }

    private static BlockNodeConfig decodeBlockNodeConfig(RawDraftContentBlock block, Map<String, String> entityMap) {
        String key = block.getKey();
        String type = block.getType();
        Map data = block.getData();
        String text = block.getText();
        int depth = block.getDepth();

        BlockNodeConfig blockNodeConfig = new BlockNodeConfig();
        blockNodeConfig.setText(text);
        blockNodeConfig.setDepth(depth);
        blockNodeConfig.setType(type != null ? type : "unstyled");
        blockNodeConfig.setKey(key != null ? key : generateRandomKey());
        blockNodeConfig.setData(data);
        blockNodeConfig.setCharacterList(decodeCharacterList(block, entityMap));

        return blockNodeConfig;
    }

    private static CharacterMetaList decodeCharacterList(RawDraftContentBlock block,
                                                        Map<String, String> entityMap) {

        String text = block.getText();
        List<RawEntityRange> rawEntityRanges = block.getEntityRanges();
        List<InlineStyleRange> rawInlineStyleRanges = block.getInlineStyleRanges();

        List<RawEntityRange> entityRanges = rawEntityRanges != null ? rawEntityRanges : new ArrayList();
        List<InlineStyleRange> inlineStyleRanges = rawInlineStyleRanges != null ? rawInlineStyleRanges : new ArrayList<>();

        // Translate entity range keys to the DraftEntity map.
        return createCharacterList(
                decodeInlineStyleRanges(text, inlineStyleRanges),
                decodeEntityRanges(
                        text,
                        entityRanges.stream()
                                .filter(range -> entityMap.containsKey(Integer.toString(range.getKey())))
                                .map(range -> new EntityRange(entityMap.get(Integer.toString(range.getKey())), range.getOffset(), range.getLength()))
                                .collect(toList()))
        );
    }
}
