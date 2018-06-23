package com.mark.bus.Controllers;

import com.mark.bus.Handy;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mark.bus.Main.DB;

public class AdminChildAddController extends AdminController {

    @FXML
    private TextField isbn,titel,auteur,uitgever,aantal;


    @FXML
    private ComboBox<String> type;

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

    @FXML void registerBook(ActionEvent event){
        // check velden ingevuld
        if (!Handy.empty(isbn.getText()) &&
            !Handy.empty(titel.getText()) &&
            !Handy.empty(auteur.getText()) &&
            !Handy.empty(uitgever.getText()) &&
            !Handy.empty(aantal.getText())){

            int boekType;
            if (type.getValue() == "Boek"){
                boekType = 0;
            }else {
                boekType = 1;
            }

            // insert row
            try {
                DB.runPreparedSql("INSERT INTO books (isbn,titel,auteur,uitgever,type,aantal) VALUES('"+isbn.getText()+"','"+titel.getText()+"','"+auteur.getText()+"','"+uitgever.getText()+"','"+boekType+"','"+aantal.getText()+"') ");
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }


    }


    @Override
    @FXML void toChange(ActionEvent event){
        try {
            VBox parentContent = FXMLLoader.load(getClass().getResource(("../resources/fxml/change.fxml")));
            root.getChildren().setAll(parentContent);
            AdminChildChangeController.setRoot(root);
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            ex.getMessage();
        }
    }


    @FXML void toPayView(ActionEvent event){
        try {
            VBox parentContent = FXMLLoader.load(getClass().getResource(("../resources/fxml/betalen.fxml")));
            root.getChildren().setAll(parentContent);
            AdminChildBetaalController.setRoot(root);
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            ex.getMessage();
        }
    }


    public void initialize(){
        ObservableList<String> options = FXCollections.observableArrayList("Boek","E-boek");
        type.setItems(options);
        type.getSelectionModel().select(0);
    }
}
