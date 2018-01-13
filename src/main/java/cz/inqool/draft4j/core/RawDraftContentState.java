package cz.inqool.draft4j.core;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class RawDraftContentState {
    private List<RawDraftContentBlock> blocks;
    private Map<String, RawDraftEntity> entityMap;
}
