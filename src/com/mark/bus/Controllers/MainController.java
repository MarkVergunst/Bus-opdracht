package com.mark.bus.Controllers;

import com.mark.bus.Handy;
import com.mark.bus.Model.BookModel;
import com.mark.bus.Model.ReserveerModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mark.bus.Main.*;


public class MainController {


    @FXML protected VBox root;

    @FXML private TableView<BookModel> table;
    @FXML private TableColumn<BookModel, String> titel;
    @FXML private TableColumn<BookModel, String> uitgeverij;
    @FXML private TableColumn<BookModel, Integer> isbn;
    @FXML private TableColumn<BookModel, String> auteur;
    @FXML private TableColumn<BookModel, String> boek;
    @FXML private TableColumn<BookModel, String> aantal;

    @FXML private TableView<ReserveerModel> reservationTable;
    @FXML private TableColumn<ReserveerModel, Long> resIsbn;
    @FXML private TableColumn<ReserveerModel, String> resTitel;
    @FXML private TableColumn<ReserveerModel, String> resUitgever;
    @FXML private TableColumn<ReserveerModel, String> resAuteur;
    @FXML private TableColumn<ReserveerModel, Date> resVan;
    @FXML private TableColumn<ReserveerModel, Date> resTot;
    @FXML private TableColumn<ReserveerModel, String> resBoek;

    @FXML private TableView<ReserveerModel> delTable,boeteTable;
    @FXML private TableColumn<ReserveerModel, Long> delIsbn,boeteIsbn;
    @FXML private TableColumn<ReserveerModel, String> delTitel, delAuteur, delUitgeverij,delBoek,boeteTitel,boeteAuteur,boeteUitgever,boeteTelaat;
    @FXML private TableColumn<ReserveerModel, Float> boeteBedrag;
    @FXML private TableColumn<ReserveerModel, Date> delVan, delTot;



    @FXML
    void Quit(ActionEvent event) {
        System.exit(0);
    }


    @FXML
    public void Reserveren(ActionEvent actionEvent) {
        try {
            isAddScreenLoaded = true;
            VBox parentContent = FXMLLoader.load(getClass().getResource(("../resources/fxml/reserveren.fxml")));
            root.getChildren().setAll(parentContent);
            reserveren.setRoot(root);

        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }


    ObservableList<BookModel> oblist = FXCollections.observableArrayList();

    private void fillObList(){
        try {
            ResultSet rs = DB.runSql("SELECT * FROM books");

            while (rs.next()){
                if (rs.getInt("aantal") > 0){
                    oblist.add(new BookModel(rs.getInt("id"),rs.getLong("isbn"),rs.getString("titel"),rs.getString("auteur"),rs.getString("uitgever"),rs.getInt("type"),rs.getInt("aantal")));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        isbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        titel.setCellValueFactory(new PropertyValueFactory<>("titel"));
        auteur.setCellValueFactory(new PropertyValueFactory<>("auteur"));
        uitgeverij.setCellValueFactory(new PropertyValueFactory<>("uitgeverij"));
        boek.setCellValueFactory(new PropertyValueFactory<>("boek"));
        aantal.setCellValueFactory(new PropertyValueFactory<>("aantal"));

        table.setItems(oblist);
    }

    @FXML void placeReservation(ActionEvent event) {
        LocalDate today = LocalDate.now();
        LocalDate next2Week = today.plus(3, ChronoUnit.WEEKS);
        BookModel book = table.getSelectionModel().getSelectedItem();


        if (!Handy.empty(String.valueOf(book))){

            try {
                DB.runPreparedSql("INSERT INTO reserveringen (book_id,gereserveerd_van,gereserveerd_tot,user_id) VALUES('"+book.getId()+"','"+today+"','"+next2Week+"','"+Controller.userId+"')");
                ResultSet rs = DB.runSql("SELECT * FROM books WHERE id ='"+book.getId()+"'");
                if (rs.next()){
                    int aantal = rs.getInt("aantal");
                    aantal--;
                    DB.runPreparedSql("UPDATE books SET aantal ='"+aantal+"' WHERE id='"+book.getId()+"'") ;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            Handy.infoBox("Reservatie is succesvol gelukt","Reserveren");
            for ( int i = 0; i<table.getItems().size(); i++) {
                table.getItems().clear();
            }

            fillObList();
            isAddScreenLoaded = false;

        }else{
            Handy.infoBox("u moet eerst een boek selecteren","Reserveren");
        }


    }

    private void fillReservationTable(TableView<ReserveerModel> table, TableColumn<ReserveerModel, Long> isbn, TableColumn<ReserveerModel, String> titel, TableColumn<ReserveerModel, String> auteur, TableColumn<ReserveerModel, String> uitgever, TableColumn<ReserveerModel, Date> van, TableColumn<ReserveerModel, Date> tot, TableColumn<ReserveerModel, String> boek) {

        ObservableList<ReserveerModel> list = FXCollections.observableArrayList();

        for ( int i = 0; i<table.getItems().size(); i++) {
            table.getItems().clear();
        }

        try {
            ResultSet rs = DB.runSql("SELECT * FROM reserveringen INNER JOIN books ON reserveringen.book_id = books.id AND reserveringen.user_id = '"+Controller.userId+"'");

            while (rs.next()){
                list.add(new ReserveerModel(rs.getString("titel"),rs.getString("auteur"),rs.getString("uitgever"),rs.getLong("isbn"),rs.getDate("gereserveerd_van"),rs.getDate("gereserveerd_tot"),rs.getInt("type"),rs.getInt("book_id")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        isbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        titel.setCellValueFactory(new PropertyValueFactory<>("titel"));
        auteur.setCellValueFactory(new PropertyValueFactory<>("auteur"));
        uitgever.setCellValueFactory(new PropertyValueFactory<>("uitgever"));
        if (van != null && tot !=null){
            van.setCellValueFactory(new PropertyValueFactory<>("van"));
            tot.setCellValueFactory(new PropertyValueFactory<>("tot"));
        }
        boek.setCellValueFactory(new PropertyValueFactory<>("boek"));


        table.setItems(list);

    }

    @FXML void toReservatonDelete(){
        try {
            isReservationDeleteListLoaded = true;
            VBox parentContent = FXMLLoader.load(getClass().getResource(("../resources/fxml/delete.fxml")));
            root.getChildren().setAll(parentContent);
            reserverenDelete.setRoot(root);
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
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
        }

    }



    @FXML public void  deleteReservation(ActionEvent event){

        ReserveerModel book = delTable.getSelectionModel().getSelectedItem();

        System.out.println(book.getId());

        if (!Handy.empty(String.valueOf(book))){

            try {
                ResultSet rs = DB.runSql("SELECT * FROM books WHERE id = '"+book.getId()+"'");
                if (rs.next()){

                    int aantal = rs.getInt("aantal");
                    aantal++;
                    DB.runPreparedSql("UPDATE books SET aantal = '"+aantal+"'");
                    ResultSet result = DB.runSql("SELECT * FROM reserveringen WHERE book_id = '"+book.getId()+"' AND user_id = '"+Controller.userId+"' ");
                    if (result.next()){
                        if (result.getInt("opgehaald")<1){

                            DB.runPreparedSql("DELETE FROM reserveringen Where book_id = '"+book.getId()+"' AND user_id = '"+Controller.userId+"'");
                            Handy.infoBox("Reservatie is succesvol geannuleerd","Reserveren");

                            for ( int i = 0; i<delTable.getItems().size(); i++) {
                                delTable.getItems().clear();
                            }

                            fillReservationTable(delTable,delIsbn,delTitel,delAuteur,delUitgeverij,delVan,delTot,delBoek);
                            isReservationDeleteListLoaded = false;

                        }else{
                            Handy.infoBox("Dit boek is al opgehaald door u daardoor kunt u deze niet annuleren.","Reserveren");
                        }
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }else{
            Handy.infoBox("u moet eerst een boek selecteren","Reserveren");
        }

    }

    public void calculateBounty() {

        LocalDate today = LocalDate.now();

        ObservableList<ReserveerModel> boete = FXCollections.observableArrayList();

        try {

            ResultSet rs = DB.runSql("SELECT *,reserveringen.id AS res_id FROM reserveringen INNER JOIN books ON reserveringen.book_id = books.id WHERE gereserveerd_tot < '" + today + "' AND user_id = '" + Controller.userId + "'");
            while (rs.next()) {

                java.util.Date sqlDate = new java.util.Date(rs.getDate("gereserveerd_tot").getTime());
                Date now = new Date();

                long diff = sqlDate.getTime() - now.getTime();
                long diffrence = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

                if (diffrence < 0) {
                    diffrence = -diffrence;
                }
                double x = (diffrence * 0.50);

                DB.runPreparedSql("UPDATE boete SET boete ='"+x+"' WHERE reservering_id ='"+rs.getInt("res_id")+"' AND user_id = '" + Controller.userId + "'");

                boete.add(new ReserveerModel(rs.getInt("res_id"), rs.getString("titel"), rs.getString("auteur"), rs.getString("uitgever"), rs.getLong("isbn"), rs.getInt("type"), rs.getInt("book_id"), diffrence, x));

                ResultSet check = DB.runSql("SELECT * FROM boete WHERE reservering_id ='" + rs.getInt("res_id") + "' AND user_id = '" + Controller.userId + "'");

                if (!check.next()) {
                    DB.runPreparedSql("INSERT INTO boete (reservering_id,book_id,boete,user_id) VALUES('" + rs.getInt("id") + "','" + rs.getInt("book_id") + "','" + x + "','" + Controller.userId + "')");
                }
            }

        } catch (SQLException xe) {
            xe.printStackTrace();
        }

        boeteIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        boeteTitel.setCellValueFactory(new PropertyValueFactory<>("titel"));
        boeteAuteur.setCellValueFactory(new PropertyValueFactory<>("auteur"));
        boeteUitgever.setCellValueFactory(new PropertyValueFactory<>("uitgever"));
        boeteTelaat.setCellValueFactory(new PropertyValueFactory<>("dagen"));
        boeteBedrag.setCellValueFactory(new PropertyValueFactory<>("bedrag"));

        boeteTable.setItems(boete);

    }


        public void initialize() {

        if (isAddScreenLoaded){
            fillObList();
            isAddScreenLoaded = false;
        }

        if (isReservationListLoaded){
            fillReservationTable(reservationTable,resIsbn,resTitel,resAuteur,resUitgever,resVan,resTot,resBoek);
            isReservationListLoaded = false;
        }
        if (isReservationDeleteListLoaded){
            fillReservationTable(delTable,delIsbn,delTitel,delAuteur,delUitgeverij,delVan,delTot,delBoek);
            isReservationDeleteListLoaded = false;
        }

        if (isBoetePageLoaded){
            calculateBounty();
            isBoetePageLoaded = false;
        }


        reserveren.init(this);
    }

}
