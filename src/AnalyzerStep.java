import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.facet.FacetField;
import org.apache.lucene.facet.FacetsConfig;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter;
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

        AnalyzerKeywords aK = new AnalyzerKeywords();

        //______________________TOKENIZAR,STEMMING, NORMALIZE______________________\\

        analyzerPerField.put("title", new EnglishAnalyzer());

        analyzerPerField.put("abstract", new EnglishAnalyzer());

        analyzerPerField.put("source", new EnglishAnalyzer());
        //_________________________________________________________________________\\

        //______________________TOKENIZAR, NORMALIZE______________________\\

        analyzerPerField.put("keywords author", aK);

        analyzerPerField.put("keywords index", aK);

        analyzerPerField.put("author", aK);

        //_________________________________________________________________________\\

        //______________________TOKENIZAR, NORMALIZE______________________\\

        analyzerPerField.put("year", new WhitespaceAnalyzer());

        analyzerPerField.put("cited by", new WhitespaceAnalyzer());

        aWrapper = new PerFieldAnalyzerWrapper(new StandardAnalyzer(),analyzerPerField);

    }

    public void indexacionArticulos(String ruta) throws Exception{

        FSDirectory directorioIndice = FSDirectory.open(Paths.get("/Users/Ivanovic/Documents/RI/practicas/P3/indice"));

        FSDirectory facetas = FSDirectory.open(Paths.get("/Users/Ivanovic/Documents/RI/practicas/P3/facetas"));

        IndexWriterConfig config = new IndexWriterConfig(aWrapper);

        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        FacetsConfig facetsconfig = new FacetsConfig();

        //Pasamos true si esta dimension puede tener mas de un valor por documento.
        facetsconfig.setMultiValued("keywords author",true);

        facetsconfig.setMultiValued("keywords index",true);

        DirectoryTaxonomyWriter taxoWriter = new DirectoryTaxonomyWriter (facetas) ;

        IndexWriter writer = new IndexWriter(directorioIndice, config);

        preprocesamientoTexto(ruta, writer, facetsconfig, taxoWriter);

        //writer.commit();

        writer.close();
        taxoWriter.close();

        scanner.close();
    }

    public String [] modificarFaceta(String cadena){

        String [] facetas = cadena.split("; ");

        return facetas;

    }

    public void preprocesamientoTexto(String ruta, IndexWriter writer, FacetsConfig facetsConfig,
                                      DirectoryTaxonomyWriter taxoWriter)throws Exception {


        scanner = new Scanner(new File(ruta));

        String content, contenidoCampo;

        int comienzoCadenaComa, comienzoCadenaComilla, finalCadena, j;

        scanner.nextLine();

        while (scanner.hasNext()) {

            content = scanner.nextLine();

            Document doc = new Document();

            for (int campo = 10; campo >= 1; campo--) {

                contenidoCampo = "";

                comienzoCadenaComa = content.indexOf(',');

                comienzoCadenaComilla = content.indexOf('"');

                if (comienzoCadenaComilla != 0 && comienzoCadenaComa != 0) {

                    finalCadena = content.indexOf(',');

                    contenidoCampo = content.substring(0, finalCadena);

                    content = content.substring(finalCadena);

                } else if ((comienzoCadenaComilla - 1) == comienzoCadenaComa ||
                        (comienzoCadenaComilla != -1 && comienzoCadenaComilla < comienzoCadenaComa)) {

                    content = content.substring(comienzoCadenaComilla + 1);

                    for (j = 0; content.charAt(j) != '"' || content.charAt(j + 1) != ','; j++) {

                        if (content.charAt(j) == '"' && content.charAt(j + 1) == '"') {
                            j++;
                        } else if (content.charAt(j) != '"') {
                            contenidoCampo += content.charAt(j);
                        }

                    }

                    content = content.substring(j + 1);

                } else {

                    content = content.substring(comienzoCadenaComa + 1);

                    if (campo != 1)
                        finalCadena = content.indexOf(',');
                    else
                        finalCadena = content.length();

                    contenidoCampo = content.substring(0, finalCadena);

                    content = content.substring(finalCadena);
                }


                insertarCampo(contenidoCampo, campo, doc);
            }

            writer.addDocument(facetsConfig.build(taxoWriter,doc));
        }
    }

    public void insertarCampo(String contenidoCampo, int campo, Document doc){

        if (campo == 10){
            //Aplicamos Analyzer para el campo Author.
            doc.add(new TextField("author", contenidoCampo, Field.Store.YES));
        }
        else if(campo == 9){
            //Aplicamos Analyzer para el campo Title.
            doc.add(new TextField("title", contenidoCampo, Field.Store.YES));
        }
        else if(campo == 8){
            //Aplicamos Analyzer para el campo Year.
            doc.add(new NumericDocValuesField("year", Integer.parseInt(contenidoCampo)));
            doc.add(new StoredField("year", Integer.parseInt(contenidoCampo)));
            doc.add(new FacetField("year", contenidoCampo));
        }
        else if(campo == 7){
            //Aplicamos Analyzer para el campo Source.
            doc.add(new TextField("source", contenidoCampo, Field.Store.YES));
        }
        else if(campo == 6){
            //Aplicamos Analyzer para el campo Cited by.
            doc.add(new NumericDocValuesField("cited by", Integer.parseInt(contenidoCampo)));
            doc.add(new StoredField("cited by", Integer.parseInt(contenidoCampo)));
            doc.add(new FacetField("cited by", contenidoCampo));
        }
        else if(campo == 5){
            //Aplicamos Analyzer para el campo Link.
            doc.add(new StringField("link", contenidoCampo, Field.Store.YES));
        }
        else if(campo == 4){
            //Aplicamos Analyzer para el campo Abstract.
            doc.add(new TextField("abstract", contenidoCampo, Field.Store.YES));
        }
        else if(campo == 3 && contenidoCampo.length() > 0){
            //Aplicamos Analyzer para el campo Keywords1.
            doc.add(new TextField("keywords author", contenidoCampo, Field.Store.YES));
            doc.add(new SortedDocValuesField("keywords author", new BytesRef(contenidoCampo)));

            String [] facetsNew = modificarFaceta(contenidoCampo);
            for (String s: facetsNew){
                doc.add(new FacetField("keywords author", s));
            }
        }
        else if(campo == 2 && contenidoCampo.length() > 0){
            //Aplicamos Analyzer para el campo Keywords2.
            doc.add(new TextField("keywords index", contenidoCampo, Field.Store.YES));
            doc.add(new SortedDocValuesField("keywords index", new BytesRef(contenidoCampo)));

            String [] facetsNew = modificarFaceta(contenidoCampo);
            for (String s: facetsNew){
                doc.add(new FacetField("keywords index", s));
            }
        }
        else if(campo == 1){
            //Aplicamos Analyzer para el campo EID.
            doc.add(new StringField("EID", contenidoCampo, Field.Store.YES));
        }
    }
}
