package com.mark.bus;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;


public class Main extends Application {

    public static Boolean isSplashLoaded = false;
    public static Boolean isAddScreenLoaded = false;
    public static Boolean isReservationListLoaded = false;
    public static Boolean isReservationDeleteListLoaded = false;
    public static Database DB = new Database("markvergunst","1VvyAFGVXdR8ohBFxOeSNF5a");
    public static Boolean isBoetePageLoaded = false;

    @Override
    public void start(Stage primaryStage) throws Exception{

//        primaryStage.initStyle(StageStyle.UNDECORATED);

        Parent root = FXMLLoader.load(getClass().getResource(("resources/fxml/login.fxml")));


        primaryStage.setTitle("Bibliotheek Uitleen Systeem");
        primaryStage.setScene(new Scene(root, 800, 550));

        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }



}
