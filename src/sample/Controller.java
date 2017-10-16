package sample;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

public class Controller {

    private View view;
    private Model model;
    private HistoryController historyController;

    public Controller(View v) {
        this.view = v;
        this.model = new Model();
        this.historyController = new HistoryController(new HistoryView(), this);
    }

    public void handleGoButton(ActionEvent e) {
        handlePageLoad(model.LoadNewPage(view.getUrlText()));
    }

    public void handleBackButton(ActionEvent e){
        String urlString = model.getPreviousUrl();
        view.setUrlTextField(urlString);
        handlePageLoad(model.loadPreviousPage(urlString));
    }

    public void handleMoveButton(ActionEvent e) {
        String urlString = view.getSelectedUrl();
        view.setUrlTextField(urlString);
        handlePageLoad(model.LoadNewPage(urlString));
    }

    public void handleListClick(MouseEvent e){
        if (e.getClickCount() == 2){
            String urlString = view.getSelectedUrl();
            view.setUrlTextField(urlString);
            handlePageLoad(model.LoadNewPage(urlString));
        }
    }

    public void handleHistoryButton(ActionEvent e){
        String urlString = historyController.showView(model.getUrlHistory(100));
        if(urlString != null){
            view.setUrlTextField(urlString);
            handlePageLoad(model.LoadNewPage(urlString));
        }
    }

    private void handlePageLoad(int result) {
        if(result == Model.SUCCESS){
            view.setInfoLabel("Current url: " + model.getCurrentUrl());
            view.setUrlList(model.getUrlList());
            view.setImageNumberLabel(model.getImageNumber());
            view.setImageSizeLabel(model.getImageSize());
        }
        else if(result == Model.WRONG_URL){
            AlertBox.display("Error", "Url does not point a website", "OK");
        }
        else if(result == Model.CONNECTION_FAILED){
            AlertBox.display("Error", "Connection failed", "OK");
        }
        else if(result == Model.NO_PREVIOUS_PAGE){
            AlertBox.display("Error", "There is no previous page", "OK");
        }
        else{
            AlertBox.display("Error", "Unspecified error", "OK");
        }
    }
}
