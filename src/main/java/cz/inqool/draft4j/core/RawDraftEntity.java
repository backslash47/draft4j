package cz.inqool.draft4j.core;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class RawDraftEntity {
    private String type;
    private String mutability;
    private Map<String, String> data;
}
