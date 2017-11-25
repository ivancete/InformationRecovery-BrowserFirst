
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class Controller {

    public void presionarBotonBuscar(ActionEvent event){
        System.out.println("Hola buscadores de artículos.");
    }

    public void presionarBotonSalir(ActionEvent event){
        Platform.exit();
    }
}
