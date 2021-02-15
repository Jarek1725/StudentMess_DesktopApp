package org.example;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.example.DB.InsertMsgToDB;
import org.example.Participans.CurrentWritter;
import org.example.Participans.UserAndMsg;
import org.example.Participans.UserSocket;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class ChatViewController {
    @FXML
    Label writerHeader;
    @FXML
    TextField msgToSend;
    @FXML
    AnchorPane anchorForScroll;

    public static VBox vboxForMess = new VBox();
    ScrollPane scrollPaneForChat = new ScrollPane();
    InsertMsgToDB insertMsgToDB = new InsertMsgToDB();

    String setConvId = null;

    public void initialize() throws IOException {

//        SET ALL MSG FROM DB
        setConvId = insertMsgToDB.getConvId(CurrentWritter.getUserId(), CurrentPerson.currPerson.getUser_id());

        try {
            ArrayList<UserAndMsg> allMsgFromDB = insertMsgToDB.getMsg(setConvId);
            System.out.println("OK");
            for (UserAndMsg userAndMsg : allMsgFromDB) {
                if(userAndMsg.getSenderId().equals(CurrentWritter.getUserId())){
                    addMsgFromOther(userAndMsg.getMsg()+" "+CurrentWritter.getUserId(), false, false);
                }else{
                    addMsgFromOther(userAndMsg.getMsg()+" "+CurrentWritter.getUserId(), true, false);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        anchorForScroll.getChildren().add(scrollPaneForChat);
        scrollPaneForChat.setPrefHeight(434);
        scrollPaneForChat.setPrefWidth(654);

//        SET STYLES
        msgToSend.setStyle("-fx-background-radius:0");
        scrollPaneForChat.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneForChat.setStyle("-fx-background: #f7f7f7;-fx-background-color:  transparent; -fx-focus-color: transparent; -fx-border-width:0");

//        SET SOCKET
        DataOutputStream dos = UserSocket.getDos();

//        SET IN HEADER WITH WHO WRITE
        Platform.runLater(()->{
            writerHeader.setText(CurrentWritter.getName()+" "+CurrentWritter.getLastName());
        });

//      MESSAGES
        vboxForMess.setPrefHeight(413);
        vboxForMess.setAlignment(Pos.BOTTOM_CENTER);
        msgToSend.setOnAction(e->{

//            SEND IN SOCKET
            try {
                dos.writeUTF(msgToSend.getText()+" "+CurrentWritter.getUserId());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
//          ADD MSG TO VIEW
            try {
                addMsgFromOther(msgToSend.getText()+" "+CurrentWritter.getUserId(),true,true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            }
            msgToSend.setText("");
        });

        vboxForMess.setSpacing(6);
        vboxForMess.setPadding(new Insets(0,0,10,0));
        setScrollPaneForChat(vboxForMess);
        scrollPaneForChat.setVvalue(1.0);

    }


//        ADD MSG FROM OTHER CLIENT BY SOCKET
    public void addMsgFromOther(String msg, boolean isMine, boolean isToDatabase) throws SQLException, ClassNotFoundException {

        if(msg.length()<2){
            return;
        }

        String msgFromWho = msg.substring(msg.lastIndexOf(" ")+1);
        String correctMsg =msg.substring(0, msg.lastIndexOf(" "));

        if(msgFromWho.equals(CurrentWritter.getUserId())){
            VBox vboxToOneMsg = new VBox();
            Label myMsg = new Label(correctMsg);
            myMsg.setWrapText(true);

            if(!isMine){
                vboxToOneMsg.setAlignment(Pos.BOTTOM_LEFT);
            }else{
                vboxToOneMsg.setAlignment(Pos.BOTTOM_RIGHT);
            }

            vboxToOneMsg.getChildren().add(myMsg);

            vboxToOneMsg.setPrefWidth(623);

            if(myMsg.getText().length()>60){
                myMsg.setMaxWidth(400);
            }

            myMsg.setStyle("-fx-background-color: #62ACB5; -fx-background-radius: 10px;");
            myMsg.setPadding(new Insets(5,10 ,5, 10));
            myMsg.setTextFill(Color.WHITE);
            vboxToOneMsg.setPadding(new Insets(0,0,0,14));

            if(isToDatabase){
                setInsertMsgToDB(correctMsg, isMine);
            }

            myMsg.setWrapText(true);

            Platform.runLater(()->{
                vboxForMess.getChildren().add(vboxToOneMsg);
                scrollPaneForChat.setVvalue(1.0);
            });

        }
    }
    private void setScrollPaneForChat(VBox vboxForMess){
        scrollPaneForChat.setContent(vboxForMess);
        scrollPaneForChat.setVvalue(1.0);
    }

    private void setInsertMsgToDB(String msg, boolean isMine){
        try {
            insertMsgToDB.insertMsg(msg, isMine?CurrentPerson.currPerson.getUser_id():CurrentWritter.getUserId(), setConvId);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
