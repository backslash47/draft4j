package cz.inqool.draft4j.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
public class DraftEntityInstance {
    private String type;
    private String mutability;
    private Map<String, String> data;
}
