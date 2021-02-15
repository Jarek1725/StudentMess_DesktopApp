package org.example.Participans;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class UserSocket {
    private static DataInputStream dis;
    private static DataOutputStream dos;
    private static Socket s;

    public static DataInputStream getDis() {
        return dis;
    }

    public static DataOutputStream getDos() {
        return dos;
    }

    public static Socket getS() {
        return s;
    }

    public static void setS(Socket s) throws IOException {
        UserSocket.s = s;
        UserSocket.dis = new DataInputStream(s.getInputStream());
        UserSocket.dos = new DataOutputStream(s.getOutputStream());
    }
}
