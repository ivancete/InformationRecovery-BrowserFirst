import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;

import java.io.*;

import java.io.File;

public class Main {

    public static void main(String [] args) throws Exception{

        String directorioDatos = System.getProperty("user.dir") + "/datosEntrada/";

        System.out.println(directorioDatos);

        File origen = new File(directorioDatos);

        TextConvertToString textToString = new TextConvertToString();

        String [] ficheros = origen.list();

        if (ficheros == null){
            return;
        }

        for (String f: ficheros) {

            if(!f.contains(".DS_Store")) {

                InputStream flujo = new FileInputStream(directorioDatos+f);

                try {

                    String content = textToString.convertToString(flujo);

                } finally {

                    flujo.close();

                }

                flujo.close();
            }
        }


    }
}
