package org.example.DB;

import org.example.Participans.UserAndMsg;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class InsertMsgToDB {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost/studentmess";
    private static final String USER = "root";
    private static final String PASS = "";

    Connection con = null;
    Statement stmt = null;

    public void insertMsg(String msgText, String senderId, String convId) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection(DB_URL, USER, PASS);

        stmt = con.createStatement();
        String sql = "INSERT INTO `messages` (`id_mess`, `conversation_id`, `sender_id`, `message_type`, `message_text`, `attachment`, `created_at`, `deleted_at`) VALUES (NULL, '"+convId+"', '"+senderId+"', 'text', '"+msgText+"', NULL, current_timestamp(), current_timestamp())";
        stmt.executeUpdate(sql);
    }

    public String getConvId(String id_first, String id_second){

        String conv_id = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = con.createStatement();
            String sql = "SELECT conversation_id FROM participants WHERE users_id IN ('"+id_first+"','"+id_second+"') GROUP BY conversation_id HAVING COUNT(*) > 1";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                conv_id = rs.getString("conversation_id");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            return conv_id;
        }
    }

    public ArrayList<UserAndMsg> getMsg(String convId) throws SQLException, ClassNotFoundException {

        ArrayList<UserAndMsg> msgFromMeAndYou = new ArrayList<>();

        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection(DB_URL, USER, PASS);
        stmt = con.createStatement();
        String sql = "SELECT * FROM messages WHERE conversation_id = '"+convId+"' ORDER BY `messages`.`created_at` DESC LIMIT 25";
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()){
            String msgText = rs.getString("message_text");
            String user_id = rs.getString("sender_id");
            msgFromMeAndYou.add(new UserAndMsg(msgText, user_id));
        }
        Collections.reverse(msgFromMeAndYou);
        return msgFromMeAndYou;
    }
}
