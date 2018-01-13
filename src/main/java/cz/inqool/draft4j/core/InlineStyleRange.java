package cz.inqool.draft4j.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.security.PrivateKey;

@AllArgsConstructor
@Getter
@Setter
public class InlineStyleRange {
    private String style;
    private int offset;
    private int length;
}
