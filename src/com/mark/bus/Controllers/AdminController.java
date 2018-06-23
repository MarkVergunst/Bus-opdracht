package com.mark.bus.Controllers;

import com.mark.bus.Handy;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mark.bus.Main.DB;

public class AdminController {

    @FXML private VBox root;

    @FXML private TextField LidNr;

    @FXML private TextField BoekNr;

    @FXML void BoekOntlenen(ActionEvent event) {

        if (!Handy.empty(LidNr.getText()) && !Handy.empty(BoekNr.getText())){

            try {
                ResultSet rs = DB.runSql("SELECT *,reserveringen.id AS res_id FROM users INNER JOIN reserveringen ON users.id = reserveringen.user_id WHERE reserveringen.book_id = '"+BoekNr.getText()+"' AND users.lidnr = '"+LidNr.getText()+"'");

                if (rs.next()){
                    DB.runPreparedSql("UPDATE reserveringen SET opgehaald = 1 WHERE id = '"+rs.getInt("res_id")+"'");
                    Handy.infoBox("Succesvol gelukt","Boek ontlenen");
                }else {
                //boek reserveren voor gebruiker

                    ResultSet getUser = DB.runSql("SELECT * FROM users WHERE lidnr = '"+LidNr.getText()+"'");

                    if (getUser.next()){
                        LocalDate today = LocalDate.now();
                        LocalDate next2Week = today.plus(3, ChronoUnit.WEEKS);

                        DB.runPreparedSql("INSERT INTO reserveringen (book_id,gereserveerd_van,gereserveerd_tot,user_id) VALUES('"+BoekNr.getText()+"','"+today+"','"+next2Week+"','"+getUser.getInt("id")+"')");
                        ResultSet resultSet = DB.runSql("SELECT * FROM books WHERE id ='"+BoekNr.getText()+"'");

                        if (rs.next()){
                            int aantal = resultSet.getInt("aantal");
                            aantal--;
                            DB.runPreparedSql("UPDATE books SET aantal ='"+aantal+"' WHERE id='"+BoekNr.getText()+"'") ;
                        }

                        Handy.infoBox("Succesvol gelukt","Boek ontlenen");

                    }

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }


        }


    }


    @FXML void addBook(ActionEvent event) {
        try {
            VBox parentContent = FXMLLoader.load(getClass().getResource(("../resources/fxml/add.fxml")));
            root.getChildren().setAll(parentContent);
            AdminChildAddController.setRoot(root);
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML void toChange(ActionEvent event){
        try {
            VBox parentContent = FXMLLoader.load(getClass().getResource(("../resources/fxml/change.fxml")));
            root.getChildren().setAll(parentContent);
            AdminChildChangeController.setRoot(root);
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML void toPayView(ActionEvent event){
        try {
            VBox parentContent = FXMLLoader.load(getClass().getResource(("../resources/fxml/betalen.fxml")));
            root.getChildren().setAll(parentContent);
            AdminChildBetaalController.setRoot(root);
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
    }





}