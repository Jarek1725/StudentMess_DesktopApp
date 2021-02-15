package org.example.DB;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.example.Person;

import java.sql.*;

public class LoginDB{
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/studentmess?useUnicode=true&amp;characterEncoding=utf8";
    static final String USER = "root";
    static final String PASS = "";

    
    public boolean login(String mail, String pass) {
        Connection conn = null;
        Statement stmt = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");

            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sql = "SELECT * FROM user WHERE email = '"+mail+"'";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String pas = rs.getString("password");
                return pas.equals(pass);
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            System.out.println(throwables.getMessage());
        }
        return false;
    }

    public Person createPerson(String email){
        Connection conn = null;
        Statement stmt = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sql = "SELECT * FROM user WHERE email = '"+email+"'";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()){
                String user_id = rs.getString("user_id");
                int is_teacher = rs.getInt("is_teacher");
                int class_id = rs.getInt("class_id");
                String nrTel = rs.getString("phone");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");

                return new Person(user_id, is_teacher, class_id, nrTel, email, firstName, lastName);
            }
    } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new Person("0", 0, 0, null, null,null, null);
    }
}