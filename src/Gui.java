import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

public class Gui extends Application {

    Stage window;
    Scene scene;
    Button buttonSearch, buttonPlus;
    ChoiceBox<String> cbFields;
    GridPane layout;
    TextField textfieldSearch, textfieldPublished1, textfieldPublished2, textfieldCited1, textfieldCited2;
    Label textLabelPublished1, textLabelPublished2, textLabelCited1, textLabelCited2;


    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage){

        window = primaryStage;
        window.setTitle("Bucador de artículos");

        layout = new GridPane();
        layout.setPadding(new Insets(2,2,2,2));

        this.createLabelPublishedCited(layout);
        this.createButtonSearch(layout);
        this.createChoiceBox(layout);
        this.createFieldSearch(layout);
        this.createFieldsPublished(layout);
        this.createFieldsCited(layout);


        scene = new Scene(layout, 800, 600);
        window.setScene(scene);
        window.show();

    }

    public void createButtonSearch(GridPane layout){

        buttonSearch = new Button("Search");
        buttonSearch.setId("buttonSearch");
        buttonSearch.setPrefWidth(100);
        buttonSearch.setTranslateX(610);
        buttonSearch.setTranslateY(10);

        buttonPlus = new Button("+");
        buttonPlus.setId("buttonPlus");
        buttonPlus.setPrefWidth(50);
        buttonPlus.setTranslateX(720);
        buttonPlus.setTranslateY(10);

        buttonPlus.setOnMouseClicked(e -> ClickSomeButton.clickButtonPlus(layout, 1));

        layout.getChildren().addAll(buttonSearch, buttonPlus);
    }

    public void createChoiceBox(GridPane layout){

        cbFields = new ChoiceBox(FXCollections.observableArrayList(
                "All Fields", "Title", "Abstract", "Author" , "Source")
        );

        cbFields.setId("choiceBox1");
        cbFields.setTranslateX(500);
        cbFields.setTranslateY(10);
        cbFields.setPrefWidth(100);
        cbFields.getSelectionModel().selectFirst();

        layout.getChildren().add(cbFields);
    }

    public void createFieldSearch(GridPane layout){
        textfieldSearch = new TextField();
        textfieldSearch.setId("fieldSearch");
        textfieldSearch.setMaxWidth(300);
        textfieldSearch.setPrefWidth(300);
        textfieldSearch.setTranslateX(200);
        textfieldSearch.setTranslateY(10);
        layout.getChildren().add(textfieldSearch);
    }

    public void createFieldsPublished(GridPane layout){
        textfieldPublished1 = new TextField();
        textfieldPublished1.setId("fieldPublished1");
        textfieldPublished1.setMaxWidth(50);
        textfieldPublished1.setPrefWidth(10);
        textfieldPublished1.setTranslateX(250);
        textfieldPublished1.setTranslateY(400);

        textfieldPublished2 = new TextField();
        textfieldPublished2.setId("fieldPublished2");
        textfieldPublished2.setMaxWidth(50);
        textfieldPublished2.setPrefWidth(10);
        textfieldPublished2.setTranslateX(400);
        textfieldPublished2.setTranslateY(400);
        layout.getChildren().addAll(textfieldPublished1, textfieldPublished2);
    }

    public void createFieldsCited(GridPane layout){
        textfieldCited1 = new TextField();
        textfieldCited1.setId("fieldCited1");
        textfieldCited1.setMaxWidth(50);
        textfieldCited1.setPrefWidth(10);
        textfieldCited1.setTranslateX(250);
        textfieldCited1.setTranslateY(500);

        textfieldCited2 = new TextField();
        textfieldCited2.setId("fieldCited2");
        textfieldCited2.setMaxWidth(50);
        textfieldCited2.setPrefWidth(10);
        textfieldCited2.setTranslateX(400);
        textfieldCited2.setTranslateY(500);
        layout.getChildren().addAll(textfieldCited1, textfieldCited2);
    }

    public void createLabelPublishedCited(GridPane layout){

        textLabelPublished1 = new Label("Published");
        textLabelPublished1.setId("labelPublished1");
        textLabelPublished1.setTranslateX(180);
        textLabelPublished1.setTranslateY(400);

        textLabelPublished2 = new Label("to");
        textLabelPublished2.setId("labelPublished2");
        textLabelPublished2.setTranslateX(350);
        textLabelPublished2.setTranslateY(400);

        textLabelCited1 = new Label("Cited by");
        textLabelCited1.setId("labelCited1");
        textLabelCited1.setTranslateX(180);
        textLabelCited1.setTranslateY(500);

        textLabelCited2 = new Label("to");
        textLabelCited2.setId("labelCited2");
        textLabelCited2.setTranslateX(350);
        textLabelCited2.setTranslateY(500);

        layout.getChildren().addAll(textLabelPublished1, textLabelPublished2, textLabelCited1, textLabelCited2);
    }
}
