import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

public class AnalyzerKeywords extends Analyzer{

    OffsetAttribute offsetAtt;

    public AnalyzerKeywords()throws Exception {

    }

    @Override
    protected Analyzer.TokenStreamComponents createComponents(String string){

        //To change body of generated methods, choose Tools | Templates.
        Tokenizer source = new MyTokenizerKeywords();

        TokenStream pipeline = source;

        pipeline = new LowerCaseFilter(pipeline);

        offsetAtt = pipeline.addAttribute(OffsetAttribute.class);


        return new Analyzer.TokenStreamComponents(source, pipeline);
    }
}
