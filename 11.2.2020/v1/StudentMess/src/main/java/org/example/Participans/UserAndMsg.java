package org.example.Participans;

public class UserAndMsg {
    String msg;
    String senderId;

    public UserAndMsg(String msg, String senderId) {
        this.msg = msg;
        this.senderId = senderId;
    }

    public String getMsg() {
        return msg;
    }

    public String getSenderId() {
        return senderId;
    }
}
