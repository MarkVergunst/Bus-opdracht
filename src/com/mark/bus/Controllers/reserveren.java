package com.mark.bus.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static com.mark.bus.Main.*;

public class reserveren extends MainController {

    private static MainController main;
    private static VBox root;


    public static void init(MainController controller) {
        main = controller;
    }

    public static void setRoot(VBox roots) {
        root = roots;
    }

    @FXML public void toMain(ActionEvent actionEvent) {
        try {
            isReservationListLoaded = true;
            VBox parentContent = FXMLLoader.load(getClass().getResource(("../resources/fxml/MainScreen.fxml")));
            root.getChildren().setAll(parentContent);

        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    @FXML void toReservatonDelete(){
        try {
            isReservationDeleteListLoaded = true;
            VBox parentContent = FXMLLoader.load(getClass().getResource(("../resources/fxml/delete.fxml")));
            root.getChildren().setAll(parentContent);
            reserverenDelete.setRoot(root);
        } catch (IOException ex) {
            ex.getMessage();
        }
    }

    @FXML void toBoetePage(){
        try {
            isBoetePageLoaded = true;
            VBox parentContent = FXMLLoader.load(getClass().getResource(("../resources/fxml/boete.fxml")));
            root.getChildren().setAll(parentContent);
            BoeteController.setRoot(root);
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            ex.getMessage();
        }

    }
}
