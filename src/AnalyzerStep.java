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
    HashMap<Integer, String> camposArticulo;

    public AnalyzerStep()throws Exception{

        //_________Estructura para saber en cada momento donde guardar los datos de un artículo__________\\

        camposArticulo = new HashMap<>();

        camposArticulo.put(1, "EID");
        camposArticulo.put(2, "keywords index");
        camposArticulo.put(3, "keywords author");
        camposArticulo.put(4, "abstract");
        camposArticulo.put(5, "link");
        camposArticulo.put(6, "cited by");
        camposArticulo.put(7, "source");
        camposArticulo.put(8, "year");
        camposArticulo.put(9, "title");
        camposArticulo.put(10, "author");

        //________________________________________________________________________\\


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

    //_______________________Función que particiona las facetas por los puntos comas y espacios_______________________\\
    public String [] modificarFaceta(String cadena){

        cadena = cadena.toLowerCase();

        String [] facetas = cadena.split("; ");

        return facetas;

    }

    //_______________________Función para realizar el preprocesamiento de los documentos CSV__________________________\\
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

    //______________________Función que inserta los datos en los campos del documento a indexar_______________________\\
    public void insertarCampo(String contenidoCampo, int campo, Document doc){

        //Introducimos los datos referentes al titulo || fuente || abstract || author.
        if ((campo == 10 || campo == 9 || campo == 7 || campo == 4) && contenidoCampo.length() > 0){

            String nombreCampo = camposArticulo.get(campo);

            doc.add(new TextField(nombreCampo, contenidoCampo, Field.Store.YES));
        }

        //Introducimos los datos referentes al año || el numero de citas.
        else if((campo == 8 || campo == 6) && contenidoCampo.length() > 0){

            String nombreCampo = camposArticulo.get(campo);

            doc.add(new NumericDocValuesField(nombreCampo, Integer.parseInt(contenidoCampo)));
            doc.add(new StoredField(nombreCampo, Integer.parseInt(contenidoCampo)));
            doc.add(new FacetField(nombreCampo, contenidoCampo));
        }

        //Aplicamos Analyzer para el campo Link.
        else if((campo == 5 || campo == 1) && contenidoCampo.length() > 0){

            String nombreCampo = camposArticulo.get(campo);

            doc.add(new StringField(nombreCampo, contenidoCampo, Field.Store.YES));
        }

        //Introducimos los datos referentes a los keywords.
        else if((campo == 3 || campo == 2) && contenidoCampo.length() > 0) {

            String nombreCampo = camposArticulo.get(campo);

            doc.add(new TextField(nombreCampo, contenidoCampo, Field.Store.YES));
            doc.add(new SortedDocValuesField(nombreCampo, new BytesRef(contenidoCampo)));

            String[] facetsNew = modificarFaceta(contenidoCampo);
            for (String s : facetsNew) {
                doc.add(new FacetField(nombreCampo, s));
            }
        }
    }
}
