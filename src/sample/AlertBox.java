package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.*;

public class AlertBox {

    public static void display(String title, String message, String buttonText){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(message);
        Button button = new Button(buttonText);
        button.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10,10,10,10));
        layout.getChildren().addAll(label, button);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

}
