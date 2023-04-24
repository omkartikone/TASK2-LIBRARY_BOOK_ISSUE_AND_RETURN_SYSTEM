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

public class IssueController {

    private Connection con;
    private Books book;

    @FXML
    private TextField bookid;
    @FXML
    private TextField rollno;
    @FXML
    private Label alert1;
    @FXML
    private Label alert2;
    @FXML
    private TableView<Books> table;
    @FXML
    private TableColumn<Books,String> Name;
    @FXML
    private TableColumn<Books,String> Number;
    @FXML
    private TableColumn<Books,String> Student;

    public void initialize(){
        Timeline changingrows=new Timeline(new KeyFrame(Duration.ZERO,event -> {
            try{
                con=DBConnection.getConnection();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            book=new Books();
            ObservableList<Books> list=FXCollections.observableArrayList();
            try{
                list=book.getIssuedBooks(con);
            }
            catch(Exception e){
                e.printStackTrace();
            }
            table.setItems(list);
            Name.setCellValueFactory(new PropertyValueFactory<Books,String>("name"));
            Number.setCellValueFactory(new PropertyValueFactory<Books,String>("id"));
            Student.setCellValueFactory(new PropertyValueFactory<Books,String>("rollno"));
        }),new KeyFrame(Duration.seconds(2))); 
        changingrows.setCycleCount(Timeline.INDEFINITE);
        changingrows.play();
    }

    public void issueBook() throws SQLException{
        con=DBConnection.getConnection();
        String query="SELECT STATUS FROM BOOKS WHERE ID='"+bookid.getText()+"';";
        ResultSet rs=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE).executeQuery(query);
        rs.first();
        if(rs.getString("STATUS").equalsIgnoreCase("ISSUED")){
            alert1.setText("This Book is Already Issued");
            alert2.setText("");
            return;
        }
        else{
            query="SELECT ROLLNO FROM BOOKS WHERE ROLLNO='"+rollno.getText()+"';";
            rs=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE).executeQuery(query);
            if(rs.next()!=false){
                alert2.setTextFill(Paint.valueOf("red"));
                alert2.setText("This student has already issued maximum number of books");
            }
            else{
                alert1.setText("");
                alert2.setTextFill(Paint.valueOf("green"));
                alert2.setText("Book Issued Successfully");
                query="UPDATE BOOKS SET STATUS='ISSUED',ROLLNO='"+rollno.getText()+"' WHERE ID='"+bookid.getText()+"';";
                con.prepareStatement(query).executeUpdate();
                return;
            }
        }
    }

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("admin");
    }
}