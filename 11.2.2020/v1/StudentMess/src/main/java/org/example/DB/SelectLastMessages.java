package org.example.DB;

//  Current user last conversation for left pane

import org.example.Participans.LastPoeple;

import java.sql.*;
import java.util.ArrayList;

public class SelectLastMessages {

    public boolean containsName(final ArrayList<LastPoeple> list, final String name){
        return list.stream().anyMatch(o -> o.getUserId().equals(name));
    }

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost/studentmess?useUnicode=true&amp;characterEncoding=utf8";
    private static final String USER = "root";
    private static final String PASS = "";

    Connection con = null;
    Statement stmt = null;

    ArrayList<LastPoeple> lastPoepleArrayList = new ArrayList<>();

    public ArrayList<LastPoeple> getLastPeople(String currentUserId) throws ClassNotFoundException, SQLException {

            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = con.createStatement();


            String sqlGetAllConversations = "SELECT user_id, messages.message_text, user.first_name, user.last_name FROM user INNER JOIN participants ON participants.users_id = user.user_id INNER JOIN messages ON messages.conversation_id = participants.conversation_id WHERE participants.conversation_id IN (SELECT participants.conversation_id FROM participants WHERE conversation_id IN (SELECT conversation_id FROM participants WHERE users_id = '"+currentUserId+"') GROUP BY participants.conversation_id) AND participants.users_id != '"+currentUserId+"' ORDER BY messages.id_mess DESC";
            ResultSet rsGetAllConversations = stmt.executeQuery(sqlGetAllConversations);

            while(rsGetAllConversations.next()){
                String user_id = rsGetAllConversations.getString("user_id");
                if(!containsName(lastPoepleArrayList, user_id)){
                    String msg = rsGetAllConversations.getString("message_text");
                    String first_name = rsGetAllConversations.getString("first_name");
                    String last_name = rsGetAllConversations.getString("last_name");

                    System.out.println(msg);

                    lastPoepleArrayList.add(new LastPoeple(first_name, last_name, user_id, msg));
                }
            }

            return lastPoepleArrayList;
    };

}
