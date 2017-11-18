import org.apache.lucene.analysis.util.CharTokenizer;

public class MyTokenizerKeywords extends CharTokenizer {

    public MyTokenizerKeywords() {
        super();
    }

    @Override
    protected boolean isTokenChar(int c) {

        return c != ';';
    }
}