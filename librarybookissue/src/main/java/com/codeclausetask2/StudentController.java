package com.codeclausetask2;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class StudentController {
    
    private Connection con;
    static String rollno;
    private Books book;
    @FXML
    private TextField bookid;
    @FXML
    private Label alert;
    @FXML
    private TableView<Books> table;
    @FXML
    private TableColumn<Books,String> Name;
    @FXML
    private TableColumn<Books,String> Number;
    @FXML
    private TableColumn<Books,String> Student;

    static public void setRollNo(String rno){
        rollno=rno;
    }

    public void initialize(){
        Timeline changingtime=new Timeline(new KeyFrame(Duration.ZERO,event->{
            book=new Books();
            try{
                con=DBConnection.getConnection();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            ObservableList<Books> list=FXCollections.observableArrayList();
            try{
                list=book.getAvailableBooks(con);
            }
            catch(Exception e){
                e.printStackTrace();
            }
            table.setItems(list);
            Name.setCellValueFactory(new PropertyValueFactory<Books,String>("name"));
            Number.setCellValueFactory(new PropertyValueFactory<Books,String>("id"));

        }),new KeyFrame(Duration.seconds(1)));
        changingtime.setCycleCount(Timeline.INDEFINITE);
        changingtime.play();
    }

    public void issueBookStudent() throws SQLException{
        con=DBConnection.getConnection();
        String query="SELECT ID FROM BOOKS WHERE ID='"+bookid.getText()+"';";
        ResultSet rs=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE).executeQuery(query);
        if(rs.next()==false){
            alert.setTextFill(Paint.valueOf("red"));
            alert.setText("Book not present in the database! Please add the book first");
        }
        else{
            query="SELECT STATUS FROM BOOKS WHERE ID='"+bookid.getText()+"';";
            rs=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE).executeQuery(query);
            rs.first();
            if(rs.getString("STATUS").equalsIgnoreCase("ISSUED")){
                alert.setText("This Book is Already Issued");
                return;
            }
            else{
                query="SELECT ROLLNO FROM BOOKS WHERE ROLLNO='"+rollno+"';";
                rs=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE).executeQuery(query);
                if(rs.next()!=false){
                    alert.setTextFill(Paint.valueOf("red"));
                    alert.setText("You have already issued maximum number of books");
                }
                else{
                    alert.setTextFill(Paint.valueOf("green"));
                    alert.setText("Book Issued Successfully");
                    query="UPDATE BOOKS SET STATUS='ISSUED',ROLLNO='"+rollno+"' WHERE ID='"+bookid.getText()+"';";
                    con.prepareStatement(query).executeUpdate();
                    return;
                }
            }
        }
    }
    
    public void returnBookStudent() throws SQLException{
        con=DBConnection.getConnection();
        String query="SELECT ID FROM BOOKS WHERE ID='"+bookid.getText()+"';";
        ResultSet rs=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE).executeQuery(query);
        if(rs.next()==false){
            alert.setTextFill(Paint.valueOf("red"));
            alert.setText("Book not present in the database! Please add the book first");
        }
        else{
            query="SELECT STATUS FROM BOOKS WHERE ID='"+bookid.getText()+"';";
            rs=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE).executeQuery(query);
            rs.first();
            if(rs.getString("STATUS").equalsIgnoreCase("AVAILABLE")){
                alert.setTextFill(Paint.valueOf("green"));
                alert.setText("Book is already returned");
            }
            else{
                query="SELECT ROLLNO FROM BOOKS WHERE ID='"+bookid.getText()+"';";
                rs=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE).executeQuery(query);
                rs.first();
                if(!rs.getString("ROLLNO").equalsIgnoreCase(rollno)){
                    alert.setTextFill(Paint.valueOf("red"));
                    alert.setText("You cannot return the book that you have not issued!!");
                }
                else{
                    query="UPDATE BOOKS SET STATUS='AVAILABLE',ROLLNO=NULL WHERE ID='"+bookid.getText()+"';";
                    con.prepareStatement(query).executeUpdate();
                    alert.setTextFill(Paint.valueOf("green"));
                    alert.setText("Book Returned");
                }
            }
        }
    }
    
    @FXML
    public void logOut() throws IOException{
        App.setRoot("login");
    }
}
