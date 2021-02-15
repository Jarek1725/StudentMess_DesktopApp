package org.example.DB;

import org.example.Person;

import java.sql.*;
import java.util.ArrayList;

public class FindUser {
    static final String DB_URL = "jdbc:mysql://localhost/studentmess?useUnicode=true&amp;characterEncoding=utf8";
    static final String USER = "root";
    static final String PASS = "";

    ArrayList<Person> people = new ArrayList<>();

    public ArrayList<Person> getUsers(String name){
        Connection conn = null;
        Statement stmt = null;

        try{
            Class.forName("com.mysql.jdbc.Driver");

            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sql = "SELECT * FROM user WHERE CONCAT(user.first_name, \" \", user.last_name) LIKE '%"+name+"%' ";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                String user_id = rs.getString("user_id");
                int is_teacher = rs.getInt("is_teacher");
                int class_id = rs.getInt("class_id");
                String nrTel = rs.getString("phone");
                String email = rs.getString("email");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");

                Person person = new Person(user_id, is_teacher, class_id, nrTel, email, firstName, lastName);

                people.add(person);

            }

            return people;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return people;
    }
}
