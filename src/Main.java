import java.io.File;

public class Main {

    public static void main(String [] args) throws Exception{

        String directorioDatos = System.getProperty("user.dir") + "/datosEntrada/";

        System.out.println(directorioDatos);

        File origen = new File(directorioDatos);

        String [] ficheros = origen.list();

        AnalyzerStep analyzerStep = new AnalyzerStep();

        if (ficheros == null){
            return;
        }

        for (String f: ficheros) {

            if(!f.contains(".DS_Store")) {

                analyzerStep.indexacionArticulos(directorioDatos+f);

            }
        }
    }
}
