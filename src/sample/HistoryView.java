package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class HistoryView {

    private Button cancelButton;
    private Button goButton;
    private ListView<String> historyList;
    private Scene scene;
    private Stage window;

    public HistoryView() {
        createScene();
    }

    public void display(){
        window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("History");
        window.setMinWidth(250);

        window.setScene(scene);
        window.showAndWait();
    }

    private void createScene() {
        historyList = new ListView<>();
        historyList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        cancelButton = new Button("Cancel");
        goButton = new Button("Go!");

        HBox bottom = new HBox(10);
        bottom.setAlignment(Pos.CENTER_RIGHT);
        bottom.setPadding(new Insets(10,10,10,10));
        bottom.getChildren().addAll(cancelButton, goButton);

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10,10,10,10));
        layout.getChildren().addAll(historyList, bottom);

        scene = new Scene(layout);
    }

    public String getSelectedUrl(){
        return historyList.getSelectionModel().getSelectedItem();
    }

    public void setGoButtonOnAction(EventHandler<ActionEvent> handler){
        goButton.setOnAction(handler);
    }

    public void setCancelButtonOnAction(EventHandler<ActionEvent> handler){
        cancelButton.setOnAction(handler);
    }

    public void setHistoryList(List<String> list){
        historyList.getItems().setAll(list);
    }

    public void closeWindow(){
        window.close();
    }
}
