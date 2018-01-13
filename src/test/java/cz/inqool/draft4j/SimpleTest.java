package cz.inqool.draft4j;

import org.junit.Test;

import java.io.IOException;

public class SimpleTest {
    @Test
    public void simpleTest() throws IOException {
        String content = "{\"entityMap\":{\"0\":{\"type\":\"LINK\",\"mutability\":\"MUTABLE\",\"data\":{\"url\":\"http://google.com\"}}},\"blocks\":[{\"key\":\"e2r8i\",\"text\":\"Header\",\"type\":\"header-one\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[],\"data\":{}},{\"key\":\"5g6ju\",\"text\":\"with some text\",\"type\":\"unstyled\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[],\"data\":{}},{\"key\":\"9ls78\",\"text\":\"and bold and italic\",\"type\":\"unstyled\",\"depth\":0,\"inlineStyleRanges\":[{\"offset\":4,\"length\":4,\"style\":\"BOLD\"},{\"offset\":13,\"length\":6,\"style\":\"ITALIC\"}],\"entityRanges\":[],\"data\":{}},{\"key\":\"bbsup\",\"text\":\"and a link\",\"type\":\"unstyled\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[{\"offset\":6,\"length\":4,\"key\":0}],\"data\":{}},{\"key\":\"6o6gr\",\"text\":\"and an ordered list:\",\"type\":\"unstyled\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[],\"data\":{}},{\"key\":\"cls6s\",\"text\":\"first\",\"type\":\"ordered-list-item\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[],\"data\":{}},{\"key\":\"5v915\",\"text\":\"second\",\"type\":\"ordered-list-item\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[],\"data\":{}},{\"key\":\"794nn\",\"text\":\"third\",\"type\":\"ordered-list-item\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[],\"data\":{}}]}";

        System.err.println(StateToHTML.convert(content));
    }
}
