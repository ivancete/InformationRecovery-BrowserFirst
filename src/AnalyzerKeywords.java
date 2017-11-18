import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.pattern.PatternTokenizer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

import java.util.regex.Pattern;

public class AnalyzerKeywords extends Analyzer{

    OffsetAttribute offsetAtt;

    public AnalyzerKeywords()throws Exception {

    }

    @Override
    protected Analyzer.TokenStreamComponents createComponents(String string){

        Pattern p = Pattern.compile("[;,]\\s");

        //To change body of generated methods, choose Tools | Templates.
        Tokenizer source = new PatternTokenizer(p, -1);

        TokenStream pipeline = source;

        pipeline = new LowerCaseFilter(pipeline);

        offsetAtt = pipeline.addAttribute(OffsetAttribute.class);


        return new Analyzer.TokenStreamComponents(source, pipeline);
    }
}
