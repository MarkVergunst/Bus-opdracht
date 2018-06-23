package com.mark.bus.Controllers;

import com.mark.bus.Handy;
import com.mark.bus.Model.BookModel;
import com.mark.bus.Model.ReserveerModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import javax.swing.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mark.bus.Main.DB;

public class AdminChildChangeController extends AdminController {

    @FXML
    private TableView<BookModel> table;

    @FXML
    private TableColumn<BookModel, String> isbn,titel,auteur,uitgever;

    @FXML
    private TableColumn<BookModel, Integer> aantal,type;


    private static VBox root;

    public static void setRoot(VBox roots) {
        root = roots;
    }

    @FXML
    void toMain(ActionEvent event) {
        try {
            VBox parentContent = FXMLLoader.load(getClass().getResource(("../resources/fxml/admin.fxml")));
            root.getChildren().setAll(parentContent);
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    @FXML void addBook(ActionEvent event) {
        try {
            VBox parentContent = FXMLLoader.load(getClass().getResource(("../resources/fxml/add.fxml")));
            root.getChildren().setAll(parentContent);
            AdminChildAddController.setRoot(root);
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            ex.getMessage();
        }
    }


    ObservableList<BookModel> bookList = FXCollections.observableArrayList();

    void fillTable(){

        try {
            ResultSet rs = DB.runSql("SELECT * FROM books");

            while(rs.next()){
                if (rs.getInt("aantal") > 0){
                    bookList.add(new BookModel(rs.getInt("id"),rs.getLong("isbn"),rs.getString("titel"),rs.getString("auteur"),rs.getString("uitgever"),rs.getInt("type"),rs.getInt("aantal")));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            e.getErrorCode();
        }

        isbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        titel.setCellValueFactory(new PropertyValueFactory<>("titel"));
        auteur.setCellValueFactory(new PropertyValueFactory<>("auteur"));
        uitgever.setCellValueFactory(new PropertyValueFactory<>("uitgeverij"));
        aantal.setCellValueFactory(new PropertyValueFactory<>("aantal"));
        type.setCellValueFactory(new PropertyValueFactory<>("boek"));

        table.setItems(bookList);
    }






    @FXML
    void changeData(ActionEvent event) {
        if (table.getSelectionModel().getSelectedItem() != null) {


            JTextField isbn = new JTextField();
            JTextField titel = new JTextField();
            JTextField auteur = new JTextField();
            JTextField uitgever = new JTextField();
            JComboBox<String> type = new JComboBox<String>();
            JTextField aantal = new JTextField();


            BookModel data = table.getSelectionModel().getSelectedItem();
            type.addItem("Boek");
            type.addItem("E-boek");

            isbn.setText(String.valueOf(data.getIsbn()));
            titel.setText(data.getTitel());
            auteur.setText(data.getAuteur());
            uitgever.setText(data.getUitgeverij());
            type.setSelectedItem(data.getBoek());
            aantal.setText(String.valueOf(data.getAantal()));

            Object[] fields = {
                    "isbn:", isbn,
                    "titel:", titel,
                    "auteur:", auteur,
                    "uitgeverij:", uitgever,
                    "Boek of E-boek:", type,
                    "aantal", aantal
            };

            int result =  JOptionPane.showConfirmDialog(null,fields,"Bewerken",JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE,null);

            if (result == JOptionPane.OK_OPTION){

                int newType;
                if (type.getSelectedItem() == "Boek"){
                    newType = 0;
                }else {
                    newType = 1;
                }


                try {
                    DB.runPreparedSql("UPDATE books SET isbn = '"+isbn.getText()+"', titel = '"+titel.getText()+"', auteur = '"+auteur.getText()+"',uitgever = '"+uitgever.getText()+"', type = '"+newType+"', aantal = '"+aantal.getText()+"'   WHERE id = '"+data.getId()+"'");
                    for ( int i = 0; i<table.getItems().size(); i++) {
                        table.getItems().clear();
                    }

                    fillTable();

                }catch (SQLException e){
                    e.printStackTrace();
                }

            }

        }else{
            Handy.infoBox("u moet eerst een veld selecteren.","Bewerken");
        }


    }

    @FXML
    void deleteData(ActionEvent event) {

        BookModel data = table.getSelectionModel().getSelectedItem();
        // check if integer and field selected
        if (table.getSelectionModel().getSelectedItem() != null){

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Weet u het zeker dat u dit boek wilt verwijderen?", ButtonType.YES, ButtonType.CANCEL);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                //do stuff
                System.out.println(data.getId());
                try {
                    DB.runPreparedSql("DELETE FROM books WHERE id='" + data.getId() + "'");


                    for ( int i = 0; i<table.getItems().size(); i++) {
                        table.getItems().clear();
                    }

                    fillTable();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }else {
            Handy.infoBox("u moet eerst een veld selecteren.","Verwijderen");
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
            ex.printStackTrace();
        }
    }

    public void initialize(){
        fillTable();
    }


}
