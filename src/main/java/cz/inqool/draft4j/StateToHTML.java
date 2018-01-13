package cz.inqool.draft4j;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.draft4j.core.ContentState;
import cz.inqool.draft4j.core.ConvertFromRawToDraftState;
import cz.inqool.draft4j.core.RawDraftContentState;
import cz.inqool.draft4j.generator.Options;

import java.io.IOException;

public class StateToHTML {
    public static String convert(String content) throws IOException {
        return convert(content, null);
    }

    public static String convert(String content, Options options) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        RawDraftContentState rawDraftContentState = mapper.readValue(content, RawDraftContentState.class);
        ContentState contentState = ConvertFromRawToDraftState.convertFromRawToDraftState(rawDraftContentState);

        return new MarkupGenerator(contentState, options).generate();
    }
}
