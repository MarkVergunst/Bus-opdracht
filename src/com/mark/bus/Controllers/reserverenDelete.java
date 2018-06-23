package com.mark.bus.Controllers;

import com.mark.bus.Controllers.MainController;
import com.mark.bus.Controllers.reserveren;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mark.bus.Main.*;

public class reserverenDelete extends MainController {

    private static VBox root;

    public static void setRoot(VBox roots) {
        root = roots;
    }

    public void toMain(ActionEvent actionEvent) {
        try {
            isReservationListLoaded = true;
            VBox parentContent = FXMLLoader.load(getClass().getResource(("../resources/fxml/MainScreen.fxml")));
            root.getChildren().setAll(parentContent);

        } catch (IOException ex) {
            ex.getMessage();
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
            ex.printStackTrace();
        }
    }


}
