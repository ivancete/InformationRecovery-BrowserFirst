import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AnalyzerStep {

    private Scanner scanner;
    private Map<String,Analyzer> analyzerPerField;
    PerFieldAnalyzerWrapper aWrapper;

    public AnalyzerStep()throws Exception{

        analyzerPerField = new HashMap<>();

        //______________________TOKENIZAR,STEMMING, NORMALIZE______________________\\
        analyzerPerField.put("title", new AnalyzerSimpleText());

        analyzerPerField.put("abstract", new AnalyzerSimpleText());

        analyzerPerField.put("source", new AnalyzerSimpleText());
        //_________________________________________________________________________\\

        //______________________TOKENIZAR, NORMALIZE______________________\\
        analyzerPerField.put("keywords author", new KeywordAnalyzer());

        analyzerPerField.put("keywords index", new KeywordAnalyzer());

        aWrapper = new PerFieldAnalyzerWrapper(new StandardAnalyzer(),analyzerPerField);

    }

    public void indexacionArticulos(String ruta) throws Exception{

        FSDirectory directorioIndice = FSDirectory.open(Paths.get("/Users/Ivanovic/Documents/RI/practicas/P3"));

        IndexWriterConfig config = new IndexWriterConfig(aWrapper);

        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        IndexWriter writer = new IndexWriter(directorioIndice, config);

        scanner = new Scanner(new File(ruta));

        String content, contenidoCampo;

        int comienzoCadenaComa, comienzoCadenaComilla, finalCadena;

        scanner.nextLine();

        while(scanner.hasNext()){

            content = scanner.nextLine();

            Document doc = new Document();

            for (int i = 10; i >= 1; i++) {

                comienzoCadenaComa = content.indexOf(',');

                comienzoCadenaComilla = content.indexOf('"');

                if (comienzoCadenaComilla != 0 && comienzoCadenaComa != 0){

                    finalCadena = content.indexOf(',');

                    //System.out.println(content.substring(0, finalCadena));

                    contenidoCampo = content.substring(0, finalCadena);

                    content = content.substring(finalCadena);

                }

                else if ((comienzoCadenaComilla-1) == comienzoCadenaComa ||
                        (comienzoCadenaComilla != -1 && comienzoCadenaComilla < comienzoCadenaComa)) {

                    content = content.substring(comienzoCadenaComilla + 1);

                    finalCadena = content.indexOf('"');

                    //System.out.println(content.substring(0, finalCadena));

                    contenidoCampo = content.substring(0, finalCadena);

                    content = content.substring(finalCadena + 1);

                } else {

                    content = content.substring(comienzoCadenaComa + 1);

                    if (i != 1)
                        finalCadena = content.indexOf(',');
                    else
                        finalCadena = content.length();

                    //System.out.println(content.substring(0, finalCadena));

                    contenidoCampo = content.substring(0, finalCadena);

                    content = content.substring(finalCadena);
                }


                if (i == 10){
                    //Aplicamos Analyzer para el campo Author.
                    doc.add(new SortedDocValuesField("author", new BytesRef(contenidoCampo)));
                }
                else if(i == 9){
                    //Aplicamos Analyzer para el campo Title.
                    doc.add(new TextField("title", contenidoCampo, Field.Store.YES));
                }
                else if(i == 8){
                    //Aplicamos Analyzer para el campo Year.
                    doc.add(new NumericDocValuesField("year", Integer.parseInt(contenidoCampo)));
                }
                else if(i == 7){
                    //Aplicamos Analyzer para el campo Source.
                    doc.add(new TextField("source", contenidoCampo, Field.Store.YES));
                }
                else if(i == 6){
                    //Aplicamos Analyzer para el campo Link.
                    doc.add(new StringField("link", contenidoCampo, Field.Store.YES));
                }
                else if(i == 5){
                    //Aplicamos Analyzer para el campo Cited by.
                    doc.add(new NumericDocValuesField("cited by", Integer.parseInt(contenidoCampo)));
                }
                else if(i == 4){
                    //Aplicamos Analyzer para el campo Abstract.
                    doc.add(new TextField("abstract", contenidoCampo, Field.Store.YES));
                }
                else if(i == 3){
                    //Aplicamos Analyzer para el campo Keywords1.
                    doc.add(new SortedDocValuesField("keywords author", new BytesRef(contenidoCampo)));
                }
                else if(i == 2){
                    //Aplicamos Analyzer para el campo Keywords2.
                    doc.add(new SortedDocValuesField("keywords index", new BytesRef(contenidoCampo)));
                }
                else if(i == 1){
                    //Aplicamos Analyzer para el campo EID.
                    doc.add(new StringField("EID", contenidoCampo, Field.Store.YES));
                }


            }
        }

        scanner.close();
    }

}
