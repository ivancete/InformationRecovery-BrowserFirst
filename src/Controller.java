
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Controller {
    @FXML
    public Button botonBuscar;
    public Button botonSalir;
    public TextField cajaBuscar;

    public void presionarBotonBuscar(ActionEvent event){

        System.out.println("Hola buscadores de artículos.");
    }

    public void presionarBotonSalir(ActionEvent event){
        Platform.exit();
    }

    public void presionarBotonBuscar2(ActionEvent event){

    }
}
