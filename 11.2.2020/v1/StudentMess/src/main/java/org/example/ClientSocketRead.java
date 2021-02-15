package org.example;

import org.example.Participans.UserSocket;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class ClientSocketRead {
    public void clientSocketStart() throws IOException{
        ChatViewController chatViewController = new ChatViewController();
        Thread readMsg = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        Socket s = UserSocket.getS();
                        DataInputStream dis = new DataInputStream(s.getInputStream());
                        String getMsg = dis.readUTF();
                        System.out.println(getMsg);
                        chatViewController.addMsgFromOther(getMsg, false, false);
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