package com.mark.bus.Controllers;

import com.mark.bus.Handy;
import com.mark.bus.Main;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;

import static com.mark.bus.Main.*;

public class Controller implements Initializable {

    @FXML private  GridPane root;

    @FXML private TextField email, usernameField, personeelsNr,pwEmail;

    @FXML private PasswordField passwordField, passwordLogin, passwordFieldl,pwOld,pwNew,pwConfirm;

    public static int userId, personeel;


    public void handleChangePassword(ActionEvent event){
        System.out.println("geklikt");

        // check velden ingevuld.
        if (!Handy.empty(pwEmail.getText()) && !Handy.empty(pwOld.getText()) && !Handy.empty(pwNew.getText()) && !Handy.empty(pwConfirm.getText())){

            try {
                ResultSet rs = DB.runSql("SELECT * FROM users WHERE email = '"+pwEmail.getText()+"'");

                if (rs.next()){

                    String DBpass = rs.getString("password");

                    byte[] salt = new byte[16];
                    char[] hashPassword = pwOld.getText().toCharArray();
                    byte[] res = hashPassword(hashPassword,salt,1,256);
                    String passwordString = Arrays.toString(res);

                    if (passwordString.equals(DBpass)){

                        if (pwNew.getText().equals(pwConfirm.getText())){
                            // hash password
                            char[] hash = pwNew.getText().toCharArray();
                            byte[] x = hashPassword(hash,salt,1,256);
                            String updatePass = Arrays.toString(x);
                            // update wachtwoord
                            DB.runPreparedSql("UPDATE users SET password = '"+updatePass+"'");


                                Parent register = FXMLLoader.load(getClass().getResource(("../resources/fxml/login.fxml")));
                                Scene scene = new Scene(register);
                                Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                appStage.setScene(scene);
                                appStage.show();


                        }

                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }catch (IOException ex){
                System.out.println(ex.getMessage());
            }

        }else {
            Handy.infoBox("U moet alle velden invullen.","Change Password");
        }
    }

    public void ToPasswordChangeView(ActionEvent event){
        try {
            Parent register = FXMLLoader.load(getClass().getResource(("../resources/fxml/changepw.fxml")));
            Scene scene = new Scene(register);
            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            appStage.setScene(scene);
            appStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleSubmitButtonAction(ActionEvent actionEvent) {
        try {
            // bus form
            String email = usernameField.getText().toLowerCase();
            String password = passwordLogin.getText();

            if (!Handy.empty(email) && !Handy.empty(password)){

                ResultSet result = DB.runSql("SELECT * FROM users WHERE email = '"+email+"'");

                if(result.next()) {
                    String dbEmail = result.getString("email");
                    int persNr = result.getInt("personeelsnr");

                    if (email.equals(dbEmail)){

                        if (persNr > 0){
                            String dbPassword = result.getString("password");

                            byte[] salt = new byte[16];
                            char[] hashPassword = password.toCharArray();
                            byte[] res = hashPassword(hashPassword,salt,1,256);
                            String passwordString = Arrays.toString(res);

                            if (passwordString.equals(dbPassword)){

                                int user = result.getInt("id");

                                userId = user;
                                personeel = persNr;

                                isReservationListLoaded = true;
                                Parent screen = FXMLLoader.load(getClass().getResource(("../resources/fxml/admin.fxml")));
                                Scene scene = new Scene(screen);
                                Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                                appStage.setScene(scene);
                                appStage.show();

                            }else{
                                // display error
                                Handy.infoBox("De bus gegevens zijn onjuist","Login Error");
                            }
                        }else {

                            String dbPassword = result.getString("password");

                            byte[] salt = new byte[16];
                            char[] hashPassword = password.toCharArray();
                            byte[] res = hashPassword(hashPassword,salt,1,256);
                            String passwordString = Arrays.toString(res);

                            if (passwordString.equals(dbPassword)){

                                int user = result.getInt("id");

                                userId = user;

                                isReservationListLoaded = true;
                                Parent screen = FXMLLoader.load(getClass().getResource(("../resources/fxml/MainScreen.fxml")));
                                Scene scene = new Scene(screen);
                                Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                                appStage.setScene(scene);
                                appStage.show();

                            }else{
                                // display error
                                Handy.infoBox("De bus gegevens zijn onjuist","Login Error");
                            }
                        }


                    }else{
                        // display error
                        Handy.infoBox("De bus gegevens zijn onjuist","Login Error");
                    }

                }


            }else{
                // display error
                Handy.infoBox("De bus gegevens zijn onjuist","Login Error");
            }


        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleRegisterButtonAction(ActionEvent actionEvent) {
        try {
            Parent register = FXMLLoader.load(getClass().getResource(("../resources/fxml/register.fxml")));
            Scene scene = new Scene(register);
            Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            appStage.setScene(scene);
            appStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleRegisterToLoginButtonAction(ActionEvent actionEvent) throws SQLException {
        // insert new user

        try{

            String emailVal = email.getText().toLowerCase();
            String password = passwordField.getText();
            String passwordConfirm = passwordFieldl.getText();

            if (password.equals(passwordConfirm)){

                if(!Handy.empty(password) && !Handy.empty(emailVal) && password.length() > 8){

                    if(Handy.checkEmail(emailVal) && Handy.passwordIsValid(password)){

                        if (!Handy.empty(personeelsNr.getText())){

                            ResultSet pers = DB.runSql("SELECT * FROM users WHERE personeelsnr = '"+personeelsNr.getText()+"'");

                            if (!pers.next()) {
                                // uw personeelsNr klopt niet.
                                Handy.infoBox(" uw personeels nummer klopt niet.","Register Error");
                            }else {
                                if (pers.next()){
                                    if (pers.getInt("personeelsnr") == 0){
                                        // uw personeelsNr klopt niet.
                                        Handy.infoBox(" uw personeels nummer klopt niet.","Register Error");
                                    }
                                }else {

                                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                                    Date date = new Date();

                                    ResultSet result = DB.runSql("SELECT MAX(lidnr) AS lidnr FROM users");
                                    result.first();
                                    int lidNr = result.getInt("lidnr");
                                    lidNr++;

                                    byte[] salt = new byte[16];
                                    char[] hashPassword = password.toCharArray();
                                    byte[] res = hashPassword(hashPassword,salt,1,256);
                                    String passwordString = Arrays.toString(res);

                                    int statement = DB.runPreparedSql("INSERT INTO users (email,password,lidNr,created_at,personeelsnr) VALUES('"+emailVal+"','"+passwordString+"','"+lidNr+"','"+dateFormat.format(date)+"',0)");


                                    //go to bus page
                                    toLoginPage(actionEvent);
                                }

                            }


                        }else{
                            // u moet personeel zijn om een lid te registren
                            Handy.infoBox("u moet personeel zijn om een lid te registren","Register Error");
                        }

                    }else{
                        // display error
                        Handy.infoBox("De wachtwoorden / email adres voldoen niet aan de requirements","Register Error");
                    }

                }else{
                    // display error
                    Handy.infoBox("De wachtwoorden / email adres voldoen niet aan de requirements","Register Error");
                }

            }else{
                // display error
                Handy.infoBox("De wachtwoorden komen niet overheen","Register Error");
            }

        }catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch (RuntimeException ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }

    }


    private static byte[] hashPassword( final char[] password, final byte[] salt, final int iterations, final int keyLength ) {

        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA512" );
            PBEKeySpec spec = new PBEKeySpec( password, salt, iterations, keyLength );
            SecretKey key = skf.generateSecret( spec );
            byte[] res = key.getEncoded( );
            return res;

        } catch( NoSuchAlgorithmException | InvalidKeySpecException e ) {
            throw new RuntimeException( e );
        }
    }

    public void toLoginPage(ActionEvent actionEvent){
        try {
            Parent register = FXMLLoader.load(getClass().getResource(("../resources/fxml/login.fxml")));
            Scene scene = new Scene(register);
            Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            appStage.setScene(scene);
            appStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

            if (!isSplashLoaded){
                loadSplashScreen();
            }
    }

    public void loadSplashScreen(){
        try {
            Main.isSplashLoaded = true;
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(("../resources/fxml/splash.fxml")));
            root.getChildren().setAll(anchorPane);

            FadeTransition fadeIn = new FadeTransition(Duration.seconds(3), anchorPane);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.setCycleCount(1);

            FadeTransition fadeOut = new FadeTransition(Duration.seconds(3), anchorPane);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setCycleCount(1);

            fadeIn.play();

            fadeIn.setOnFinished((e)->{
                fadeOut.play();
            });

            fadeOut.setOnFinished((e)->{
                try {
                    GridPane parrentContent = FXMLLoader.load(getClass().getResource(("../resources/fxml/login.fxml")));
                    root.getChildren().setAll(parrentContent);



                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
