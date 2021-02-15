package org.example;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.example.DB.LoginDB;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LoginViewController {

    @FXML
    JFXButton LoginBtn;
    @FXML
    JFXButton exitBtn;
    @FXML
    Label textToChange;
    @FXML
    private JFXPasswordField passwdText;
    @FXML
    private JFXTextField emailText;
    @FXML
    Label loginError;

    private double xOffset = 0;
    private double yOffset = 0;

    public void initialize(){

        LoginDB loginDB = new LoginDB();

        EventHandler<ActionEvent> loginClick = event -> {
            String email = emailText.getText();
            String passwd = passwdText.getText();

            if(loginDB.login(email, passwd)){
                CurrentPerson.currPerson = loginDB.createPerson(email);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    LoggedUserController loggedUserController = new LoggedUserController();
                                    Stage stage = (Stage) LoginBtn.getScene().getWindow();
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("LoggedUserView.fxml"));
                                    loader.setController(loggedUserController);
                                    Parent parent = loader.load();
                                    Scene scene = new Scene(parent);
                                    parent.setOnMousePressed(new EventHandler<MouseEvent>() {
                                        @Override
                                        public void handle(MouseEvent event) {
                                            xOffset = event.getSceneX();
                                            yOffset = event.getSceneY();
                                        }
                                    });
                                    parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
                                        @Override
                                        public void handle(MouseEvent event) {
                                            stage.setX(event.getScreenX() - xOffset);
                                            stage.setY(event.getScreenY() - yOffset);
                                        }
                                    });
                                    stage.setScene(scene);
                                    stage.show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }).start();
            } else loginError.setText("Błędny login lub hasło");
        };

        EventHandler<ActionEvent> exitClick = event -> System.exit(0);

        LoginBtn.setOnAction(loginClick);
        exitBtn.setOnAction(exitClick);
    }
}
