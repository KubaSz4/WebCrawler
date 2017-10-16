package sample;

import javafx.event.ActionEvent;
import java.util.List;

public class HistoryController {
    private Controller controller;
    private HistoryView view;
    private volatile String urlString;

    public HistoryController(HistoryView view, Controller controller) {
        this.view = view;
        view.setCancelButtonOnAction(e -> handleCancelButton(e));
        view.setGoButtonOnAction(e -> handleGoButton(e));
        this.controller = controller;
    }

    public String showView(List<String> urlHistory) {
        view.setHistoryList(urlHistory);
        urlString = null;
        view.display();
        //urlString may be assigned by handleGoButton method
        return urlString;
    }

    public void handleGoButton(ActionEvent e){
        urlString = view.getSelectedUrl();
        view.closeWindow();
    }

    public void handleCancelButton(ActionEvent e){
        view.closeWindow();
    }
}
