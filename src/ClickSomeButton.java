import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class ClickSomeButton {

    Button buttonPlus, buttonLess;

    public static void clickButtonSearch(){

    }

    public static void clickButtonPlus(GridPane layout, int cantidad){

        Button buttonPlus = new Button("+");
        buttonPlus.setId("buttonPlus"+cantidad);
        buttonPlus.setPrefWidth(50);
        buttonPlus.setTranslateX(720);
        buttonPlus.setTranslateY(100*cantidad);

        buttonPlus.setOnMouseClicked(e -> clickButtonPlus(layout, cantidad+1));

        TextField textfieldSearch = new TextField();
        textfieldSearch.setId("fieldSearch"+cantidad);
        textfieldSearch.setMaxWidth(300);
        textfieldSearch.setPrefWidth(300);
        textfieldSearch.setTranslateX(200);
        textfieldSearch.setTranslateY(100*cantidad);
        layout.getChildren().addAll(buttonPlus, textfieldSearch);


    }
}
