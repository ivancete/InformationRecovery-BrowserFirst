import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;

import java.io.InputStream;

public class TextConvertToString {

    public TextConvertToString(){

    }

    public String convertToString(InputStream textStream)throws Exception, TikaException {

        try{

            Metadata metadata = new Metadata();

            //Le pasamos el -1 para que no nos salte un error de que el fichero sobrepasa el límite de caracteres.
            BodyContentHandler handler = new BodyContentHandler(-1);

            ParseContext parseContext = new ParseContext();

            AutoDetectParser parser = new AutoDetectParser();

            parser.parse(textStream, handler, metadata, parseContext);

            return handler.toString();
        }
        finally {

        }
    }

}
