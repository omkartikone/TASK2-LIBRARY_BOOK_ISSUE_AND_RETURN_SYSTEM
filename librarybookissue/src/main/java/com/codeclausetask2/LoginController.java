package com.codeclausetask2;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;

public class LoginController {
    
    private Connection con;
    @FXML
    private TextField userid;
    @FXML
    private PasswordField password;
    @FXML
    private Label alert;

    @FXML
    public void initialize() throws SQLException{
        con=DBConnection.getConnection();
    }
    public void loginStudent() throws SQLException, IOException{
        String query="SELECT ROLE,ROLLNO FROM STUDENTS WHERE USERID='"+userid.getText()+"' AND PASSWORD='"+password.getText()+"';";
        ResultSet rs=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE).executeQuery(query);
        if(rs.next()==false){
            alert.setTextFill(Paint.valueOf("red"));
            alert.setText("Invalid Login Credentials");
        }
        else{
            if(rs.getString("ROLE").equalsIgnoreCase("ADMIN")){
                App.setRoot("admin");
            }
            else{
                StudentController.setRollNo(rs.getString("ROLLNO"));
                App.setRoot("student");
            }
        }
    }
}
