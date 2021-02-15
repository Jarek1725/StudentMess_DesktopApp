package org.example;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import org.example.DB.FindUser;
import org.example.DB.SelectLastMessages;
import org.example.Participans.CurrentWritter;
import org.example.Participans.LastPoeple;
import org.example.Participans.UserSocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class LoggedUserController {

    @FXML
    ImageView studentBtn;
    @FXML
    ImageView exitBtn;
    @FXML
    Label UserNameLabel;
    @FXML
    TextField  searchText;
    @FXML
    VBox blurVbox;
    @FXML
    ScrollPane sbForPoeple;
    @FXML
    AnchorPane rootPane;
    @FXML
    AnchorPane rightPaneToActions;

    Socket s = null;

    public static ArrayList<Button> buttonArrayList = new ArrayList<>();
    String musicFile = "org/example/Img/Google_Event.mp3";

    VBox seachVbox = new VBox();

    public void initialize() throws SQLException, ClassNotFoundException, IOException {
        seachVbox.setStyle("-fx-focus-color: transparent;");

        sbForPoeple.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sbForPoeple.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

//        SET USER NAME
        UserNameLabel.setText(CurrentPerson.currPerson.getFirstName() + " " + CurrentPerson.currPerson.getLastName());

//        CREATE USER SOCKET
        s = new Socket("localhost", 8989);
        String userId = String.valueOf(CurrentPerson.currPerson.user_id);
        ClientSocketRead clsr = new ClientSocketRead();
        clientSocketStart();
        UserSocket.setS(s);
        UserSocket.getDos().writeUTF(userId);

//        LEFT BAR CURSOR
        studentBtn.setCursor(Cursor.HAND);
        exitBtn.setCursor(Cursor.HAND);

//        LEFT BAR ACTIONS
        studentBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("User View");
            }
        });

//        SET RANDOM BACKGROUND IMAGE
        Random randomBcg = new Random();
        int random = randomBcg.nextInt(6);
        rootPane.setStyle("-fx-background-image: url(\"org/example/Img/Backgrounds/"+(random+1)+".jpg\");");


//        ADD TO LEFT PANE LAST CONVERSATIONS
        setLastConversations();
        sbForPoeple.setContent(seachVbox);

//        ADD TO LEFT PANE FROM SEARCH
        searchText.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty()){
                seachVbox.getChildren().removeAll(seachVbox.getChildren());
                System.out.println(newValue);
                FindUser findUser = new FindUser();
                ArrayList<Person> people = findUser.getUsers(newValue);
                for (Person person : people) {
                    Button label = new Button(person.getFirstName()+" "+person.getLastName());
                    label.setTextFill(Color.WHITE);
                    label.setAlignment(Pos.BASELINE_LEFT);
                    label.setFont(Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 14));
                    label.setPadding(new Insets(9,5,9,22));
                    label.setStyle("-fx-background-color:  #1c1c1c;");
                    label.setPrefWidth(277);
                    seachVbox.getChildren().add(label);
                    sbForPoeple.setStyle("-fx-background: #1c1c1c;-fx-background-color:  #1c1c1c; -fx-focus-color: transparent;");


//                    TO ROB
//                    label.setOnAction(new EventHandler<ActionEvent>() {
//                        @Override
//                        public void handle(ActionEvent event) {
//
//                        }
//                    });

                }
            } else{
                try {
                    setLastConversations();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            sbForPoeple.setContent(seachVbox);

        });


//  EXIT FROM PROGRAM
        exitBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    DataOutputStream dos = UserSocket.getDos();
                    dos.writeUTF(CurrentPerson.currPerson.user_id + " Logout");
//                    dis.close();
                    dos.close();
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
        });
    }


//    OPEN CHAT IN RIGHT SITE
    public void setCurrentWritter(String firstName, String lastName, String userId) throws SQLException, ClassNotFoundException {
        setLastConversations();
        CurrentWritter.setName(firstName);
        CurrentWritter.setLastName(lastName);
        CurrentWritter.setUserId(userId);
//        REMOVE OLD MESSAGES
        ChatViewController.vboxForMess.getChildren().removeAll(ChatViewController.vboxForMess.getChildren());

//        SET BTN NEW COLOR
        for (Button button : buttonArrayList) {
            if(button.getId().equals(CurrentWritter.getUserId())) {
                button.setStyle("-fx-background-color:  #424242;");
            }else{
                button.setStyle("-fx-background-color:  #1c1c1c;");
            }
        }

//      ADD CHAT TO RIGHT SCENE
        Platform.runLater(()->{
            try {
//          POPRAWA JESLI DODAM  WIECEJ (ZROBIENIE W FUNKCJI - MNIEJ KODU)
                FXMLLoader chatLoader = new FXMLLoader(getClass().getResource("ChatView.fxml"));
                ChatViewController chatViewController = new ChatViewController();
                chatLoader.setController(chatViewController);
                Parent parent = chatLoader.load();
                rightPaneToActions.getChildren().add(parent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    

    void setLastConversations() throws SQLException, ClassNotFoundException{

        buttonArrayList.clear();
        SelectLastMessages selectLastMessages = new SelectLastMessages();
        System.out.println("USTAWIA");
        seachVbox.getChildren().removeAll(seachVbox.getChildren());
        ArrayList<LastPoeple> selectLastMessagesArrayList = selectLastMessages.getLastPeople(CurrentPerson.currPerson.getUser_id());

        for (LastPoeple lastPoeple : selectLastMessagesArrayList) {
            Button personBtn = new Button(lastPoeple.getFirstName()+" "+lastPoeple.getLastName());
            Button msgBtn = new Button(lastPoeple.getLastMsg());

            personBtn.setId(lastPoeple.getUserId());
            msgBtn.setId(lastPoeple.getUserId());
            buttonArrayList.add(personBtn);
            buttonArrayList.add(msgBtn);

            personBtn.setOnAction(e-> {
                try {
                    setCurrentWritter(lastPoeple.getFirstName(), lastPoeple.getLastName(), lastPoeple.getUserId());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException classNotFoundException) {
                    classNotFoundException.printStackTrace();
                }
            });
            msgBtn.setOnAction(e-> {
                try {
                    setCurrentWritter(lastPoeple.getFirstName(), lastPoeple.getLastName(), lastPoeple.getUserId());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException classNotFoundException) {
                    classNotFoundException.printStackTrace();
                }
            });

            personBtn.setAlignment(Pos.BASELINE_LEFT);
            msgBtn.setAlignment(Pos.BASELINE_LEFT);
            personBtn.setTextFill(Color.WHITE);
            msgBtn.setTextFill(Color.WHITE);
            personBtn.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 14));
            msgBtn.setFont(Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 12));
            personBtn.setPadding(new Insets(12,5,2,22));
            msgBtn.setPadding(new Insets(2,5,12,22));
            personBtn.setStyle("-fx-background-color:  #1c1c1c;");
            msgBtn.setStyle("-fx-background-color: #1c1c1c; -fx-border-width:0 0 1 0; -fx-border-color: #2e2e2e;");
            personBtn.setPrefWidth(287);
            msgBtn.setPrefWidth(287);

            if(lastPoeple.getUserId().equals(CurrentWritter.getUserId())){
                personBtn.setStyle("-fx-background-color:  #424242;");
                msgBtn.setStyle("-fx-background-color: #424242; -fx-border-width:0 0 1 0; -fx-border-color: #2e2e2e;");
            }

            seachVbox.getChildren().add(personBtn);
            seachVbox.getChildren().add(msgBtn);

            sbForPoeple.setStyle("-fx-background: #1c1c1c;-fx-background-color:  transparent; -fx-focus-color: transparent;");
        }
        sbForPoeple.setContent(seachVbox);
    }

    public void clientSocketStart() throws IOException{
        ChatViewController chatViewController = new ChatViewController();
        Thread readMsg = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
//                        Socket s = UserSocket.getS();
                        DataInputStream dis = new DataInputStream(s.getInputStream());
                        String getMsg = dis.readUTF();
                        System.out.println(getMsg);
                        chatViewController.addMsgFromOther(getMsg, false, false);
                        Platform.runLater(()->{
                            try {
                                setLastConversations();
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        });
                    } catch (IOException e) {
                        try {
                            UserSocket.getDis().close();
                            UserSocket.getDos().close();
                            break;
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        readMsg.start();
    }

}
