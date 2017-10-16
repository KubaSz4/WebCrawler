package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;


public class View extends Application {

    private Controller controller;
    private Button goButton;
    private TextField urlTextField;
    private Label infoLabel;
    private Label imageNumberLabel;
    private ListView<String> urlList;
    private Button backButton;
    private Button moveButton;
    private Scene scene;
    private Label imageSizeLabel;
    private Button historyButton;

    public View() {
        controller = new Controller(this);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("WebCrawler");

        createScene();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createScene() {
        VBox layout = new VBox(5);
        layout.setPadding(new Insets(10,10,10,10));

        HBox top = new HBox(5);
        urlTextField = new TextField();
        urlTextField.setOnAction(e -> controller.handleGoButton(e));
        HBox.setHgrow(urlTextField, Priority.ALWAYS);
        goButton = new Button("GO!");
        goButton.setOnAction(e -> controller.handleGoButton(e));
        top.getChildren().addAll(urlTextField, goButton);

        infoLabel = new Label("Current url: XXX");
        imageNumberLabel = new Label("Number of images: XXX");
        imageSizeLabel = new Label("Size of images: XXX");

        urlList = new ListView<String>();
        urlList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        urlList.setOnMouseClicked(e -> controller.handleListClick(e));

        HBox bottom = new HBox(5);
        bottom.setAlignment(Pos.CENTER_RIGHT);
        backButton = new Button("Back");
        backButton.setOnAction(e -> controller.handleBackButton(e));
        historyButton = new Button("History");
        historyButton.setOnAction(e -> controller.handleHistoryButton(e));
        moveButton = new Button("Move");
        moveButton.setOnAction(e -> controller.handleMoveButton(e));
        bottom.getChildren().addAll(backButton, historyButton, moveButton);
        layout.getChildren().addAll(top, infoLabel, imageNumberLabel, imageSizeLabel, urlList, bottom);

        scene = new Scene(layout, 500, 475);
    }

    public String getUrlText(){
        return urlTextField.getText();
    }

    public String getSelectedUrl(){
        return urlList.getSelectionModel().getSelectedItem();
    }

    public void setUrlList(List<String> list){
        urlList.setItems(FXCollections.observableArrayList(list));
    }

    public void setInfoLabel(String str){
        infoLabel.setText(str);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void setImageNumberLabel(int imageNumber) {
        imageNumberLabel.setText("Number of images: " + imageNumber);
    }

    public void setImageSizeLabel(long imageSize) {
        imageSizeLabel.setText("Size of images: " + imageSize);
    }

    public void setUrlTextField(String urlText){
        urlTextField.setText(urlText);
    }
}
