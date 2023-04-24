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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

public class LandingController {

    private Books book;
    private Connection con;

    @FXML
    private Label alert1;
    @FXML
    private Label alert2;
    @FXML
    private TextField bookname;
    @FXML
    private TextField bookid;
    @FXML
    private TextField studname;
    @FXML
    private PasswordField password;
    @FXML
    private TextField userid;
    @FXML
    private TextField rollno;
    @FXML
    private TableView<Books> table;
    @FXML
    private TableColumn<Books,String> Name;
    @FXML
    private TableColumn<Books,String> Number;

    public void initialize(){
        Timeline changingBooks=new Timeline(new KeyFrame(Duration.ZERO,event->{
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
        changingBooks.setCycleCount(Timeline.INDEFINITE);
        changingBooks.play();
    }

    @FXML
    private void returnBook() throws IOException {
        App.setRoot("return");
    }
    
    @FXML
    private void issueBook() throws IOException {
        App.setRoot("issue");
    }

    @FXML
    public void addBook() throws SQLException{
        con=DBConnection.getConnection();
        String query="SELECT ID FROM BOOKS WHERE ID='"+bookid.getText()+"';";
        ResultSet rs=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE).executeQuery(query);
        if(rs.next()!=false){
            alert1.setTextFill(Paint.valueOf("red"));
            alert1.setText("Book already exists");
        }
        else{
            query="INSERT INTO BOOKS(NAME,ID,STATUS) VALUES('"+bookname.getText()+"','"+bookid.getText()+"','AVAILABLE');";
            con.prepareStatement(query).executeUpdate();
            alert1.setTextFill(Paint.valueOf("green"));
            alert1.setText("Book successfully added to the database");
        }
    }

    @FXML
    public void addStudent() throws SQLException{
        con=DBConnection.getConnection();
        String query="SELECT USERID FROM STUDENTS WHERE USERID='"+userid.getText()+"';";
        ResultSet rs=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE).executeQuery(query);
        if(rs.next()!=false){
            alert2.setTextFill(Paint.valueOf("red"));
            alert2.setText("UserId already exists!! Enter a different One");
        }
        else{
            query="SELECT ROLLNO FROM STUDENTS WHERE ROLLNO='"+rollno.getText()+"';";
            rs=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE).executeQuery(query);
            if(rs.next()!=false){
                alert2.setTextFill(Paint.valueOf("red"));
                alert2.setText("Roll Number already exists!! Enter a different One");
            }
            else{
                query="INSERT INTO STUDENTS(NAME,ROLLNO,USERID,PASSWORD,ROLE) VALUES('"+studname.getText()+"','"+rollno.getText()+"','"+userid.getText()+"','"+password.getText()+"','STUDENT');";
                con.prepareStatement(query).executeUpdate();
                alert2.setTextFill(Paint.valueOf("green"));
                alert2.setText("Student successfully added to the database");
            }
        }
    }

    @FXML
    public void logOut() throws IOException{
        App.setRoot("login");
    }
}
