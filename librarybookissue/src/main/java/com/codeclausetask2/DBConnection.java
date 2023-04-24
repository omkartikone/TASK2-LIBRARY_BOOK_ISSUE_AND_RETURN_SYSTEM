package com.codeclausetask2;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String url="jdbc:mysql://localhost:3306/CODECLAUSETASK2";
    private static final String user="root";
    private static final String pass="MarathaSQL@6124";

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(url,user,pass);
    }
}
