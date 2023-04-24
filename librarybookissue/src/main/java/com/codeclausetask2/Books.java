package com.codeclausetask2;

import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Books {
    private String name;
    private String id;
    private String status;
    private String rollno;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getRollno() {
        return rollno;
    }
    public void setRollno(String rollno) {
        this.rollno = rollno;
    }
    public void saveBook(Connection con) throws SQLException{
        String query="INSERT INTO BOOKS VALUES('"+name+"','"+id+"','"+status+"','"+rollno+"')";
        con.prepareStatement(query).executeUpdate();
    }
    public ObservableList<Books> getAvailableBooks(Connection con) throws SQLException{
        String query="SELECT * FROM BOOKS WHERE STATUS LIKE 'AVAILABLE'";
        ResultSet rs=con.prepareStatement(query).executeQuery();        
        ObservableList<Books> list=FXCollections.observableArrayList();
        while(rs.next()){
            Books B=new Books();
            B.setName(rs.getString("NAME"));
            B.setId(rs.getString("ID"));
            B.setStatus(rs.getString("STATUS"));
            B.setRollno(rs.getString("ROLLNO"));
            list.add(B);
        }
        return list;
    }
    public ObservableList<Books> getIssuedBooks(Connection con) throws SQLException{
        String query="SELECT * FROM BOOKS WHERE STATUS NOT LIKE 'AVAILABLE'";
        ResultSet rs=con.prepareStatement(query).executeQuery();        
        ObservableList<Books> list=FXCollections.observableArrayList();
        while(rs.next()){
            Books B=new Books();
            B.setName(rs.getString("NAME"));
            B.setId(rs.getString("ID"));
            B.setStatus(rs.getString("STATUS"));
            B.setRollno(rs.getString("ROLLNO"));
            list.add(B);
        }
        return list;
    }
}

