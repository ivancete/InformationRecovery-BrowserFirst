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

                try{

                    String content = textToString.convertToString(flujo);

                    content = content.substring(content.indexOf('\n')+1);

                    int i;

                    while (content.length() > 1){

                        if (content.indexOf('\"') != 0 && content.indexOf(',') != 0){

                            i = content.indexOf(',');

                            System.out.println(content.substring(0, i));

                            content = content.substring(i+1);

                        }

                        else if ((content.indexOf('\"')-1) == content.indexOf(',') ||
                        (content.indexOf('\"') < content.indexOf(',') && content.indexOf('\"') < content.length())) {

                            content = content.substring(content.indexOf('\"')+1);

                            if (content.indexOf('\"') < content.indexOf('\n')){
                                i = content.indexOf('\"');
                            }
                            else{
                                i = content.indexOf('\n')+1;
                            }

                            System.out.println(content.substring(0, i));

                            content = content.substring(i+1);
                        }
                        else {

                            content = content.substring(content.indexOf(',')+1);

                            if (content.indexOf(',') < content.indexOf('\n')){
                                i = content.indexOf(',');
                            }
                            else{
                                i = content.indexOf('\n')+1;
                            }

                            System.out.println(content.substring(0, i));

                            content = content.substring(i);
                        }
                    }

                } finally {

                    flujo.close();

                }
            }
        }
    }
}
