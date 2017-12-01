
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class Controller extends AnchorPane{

    @FXML
    private Button botonBuscar;
    @FXML
    private Button botonSalir;
    @FXML
    private TextField cajaBuscar;
    @FXML
    public ArrayList<TextField> cajasBooleanas;
    @FXML
    private ChoiceBox cajaCamposBusqueda;
    @FXML
    private Label cosas;
    @FXML
    private ListView p;

    protected List<String> asianCurrencyList = new ArrayList<>();
    protected List<String> europeanCurrencyList = new ArrayList<>();
    protected ListProperty<String> listProperty = new SimpleListProperty<>();


    public void initialize(){

        cajasBooleanas = new ArrayList<>();

        botonBuscar.setText("Buscar");
        botonSalir.setText("Salir");
        List<String> campos = new ArrayList<>();
        campos.add("Todos los campos");
        campos.add("Título");
        campos.add("Autor");
        campos.add("Abstract");
        campos.add("Fuente");
        ObservableList obList = FXCollections.observableList(campos);
        cajaCamposBusqueda.setItems(obList);

        asianCurrencyList.add("CNH");
        asianCurrencyList.add("JPY");
        asianCurrencyList.add("HKD");
        asianCurrencyList.add("KRW");
        asianCurrencyList.add("SGD");

        europeanCurrencyList.add("EUR");
        europeanCurrencyList.add("GBP");
        europeanCurrencyList.add("NOK");
        europeanCurrencyList.add("SEK");
        europeanCurrencyList.add("CHF");
        europeanCurrencyList.add("HUF");

        p.itemsProperty().bind(listProperty);

        listProperty.set(FXCollections.observableArrayList(asianCurrencyList));
    }

    public void presionarBotonBuscar(ActionEvent event){

        System.out.println("Hola buscadores de artículos.");

    }

    public void presionarBotonSalir(ActionEvent event)
    {

        Platform.exit();
    }

    public void presionarBotonMas(ActionEvent event){

        listProperty.set(FXCollections.observableArrayList(europeanCurrencyList));
    }
}
