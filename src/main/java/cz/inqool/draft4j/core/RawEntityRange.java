package cz.inqool.draft4j.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RawEntityRange {
    private int key;
    private int offset;
    private int length;
}
