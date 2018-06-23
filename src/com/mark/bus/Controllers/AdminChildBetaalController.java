package com.mark.bus.Controllers;

import com.mark.bus.Handy;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;

import java.awt.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mark.bus.Main.DB;

public class AdminChildBetaalController extends AdminController {

    @FXML public TextField LidNr;

    private static VBox root;

    public static void setRoot(VBox roots) {
        root = roots;
    }

    @FXML void toMain(ActionEvent event) {
        try {
            VBox parentContent = FXMLLoader.load(getClass().getResource(("../resources/fxml/admin.fxml")));
            root.getChildren().setAll(parentContent);
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    @FXML
    void addBook(ActionEvent event) {
        try {
            VBox parentContent = FXMLLoader.load(getClass().getResource(("../resources/fxml/add.fxml")));
            root.getChildren().setAll(parentContent);
            AdminChildAddController.setRoot(root);
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            ex.getMessage();
            ex.printStackTrace();
            ex.fillInStackTrace();
        }
    }


    @FXML void BoeteBetalen(ActionEvent event){

        if (!Handy.empty(LidNr.getText())){
            try {
                ResultSet getlid = DB.runSql("SELECT * FROM users WHERE lidnr = '"+LidNr.getText()+"'");
                if (getlid.next()){

                    int lid_id = getlid.getInt("id");

                    ResultSet getBoetes = DB.runSql("SELECT * FROM boete WHERE user_id = '"+lid_id+"'");

                    double x=0;
                    while (getBoetes.next()){

                     x += getBoetes.getDouble("boete");

                    }

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "De openstaande boete Betreft: € " +  x + " Dit moet in 1 keer betaald worden. Als u op 'Yes' klikt dan is de boete betaald.", ButtonType.YES, ButtonType.CANCEL);
                    alert.showAndWait();

                    if (alert.getResult() == ButtonType.YES) {
                        //verwijder boete
                        DB.runPreparedSql("DELETE * FROM boete WHERE user_id = '"+lid_id+"'");
                    }



                }else {
                    Handy.infoBox("dit nummer is geen geldig lid nummer.","boetes");
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}
