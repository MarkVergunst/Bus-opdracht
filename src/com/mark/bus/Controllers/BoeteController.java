package com.mark.bus.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mark.bus.Main.isAddScreenLoaded;
import static com.mark.bus.Main.isReservationDeleteListLoaded;
import static com.mark.bus.Main.isReservationListLoaded;

public class BoeteController extends MainController {

    private static VBox root;

    public static void setRoot(VBox roots) {
        root = roots;
    }

    @FXML public void toMain(ActionEvent actionEvent) {
        try {
            isReservationListLoaded = true;
            VBox parentContent = FXMLLoader.load(getClass().getResource(("../resources/fxml/MainScreen.fxml")));
            root.getChildren().setAll(parentContent);

        } catch (IOException ex) {
            ex.getMessage();
            ex.printStackTrace();
        }
    }

    @FXML void toReservatonDelete(){
        try {
            isReservationDeleteListLoaded = true;
            VBox parentContent = FXMLLoader.load(getClass().getResource(("../resources/fxml/delete.fxml")));
            root.getChildren().setAll(parentContent);
            reserverenDelete.setRoot(root);
        } catch (IOException ex) {
            ex.getMessage();
            ex.printStackTrace();
        }
    }

    @Override
    @FXML public void Reserveren(ActionEvent event) {
        try {
            isAddScreenLoaded = true;
            VBox parentContent = FXMLLoader.load(getClass().getResource(("../resources/fxml/reserveren.fxml")));
            root.getChildren().setAll(parentContent);
            reserveren.setRoot(root);

        } catch (IOException ex) {
            ex.getMessage();
            ex.printStackTrace();
        }
    }


}
