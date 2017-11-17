import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

public class AnalyzerSimpleText extends Analyzer{

    // Tokens longer than this length are discarded. Defaults to 50 chars. */
    OffsetAttribute offsetAtt;


    public AnalyzerSimpleText()throws Exception {

    }

    @Override
    protected Analyzer.TokenStreamComponents createComponents(String string){

        //To change body of generated methods, choose Tools | Templates.

        Tokenizer source = new StandardTokenizer();

        TokenStream pipeline = source;
        pipeline = new StandardFilter(pipeline);

        //pipeline = new EnglishPossessiveFilter(pipeline);

        pipeline = new LowerCaseFilter(pipeline);
        pipeline = new PorterStemFilter(pipeline);

        offsetAtt = pipeline.addAttribute(OffsetAttribute.class);


        return new Analyzer.TokenStreamComponents(source, pipeline);
    }
}
