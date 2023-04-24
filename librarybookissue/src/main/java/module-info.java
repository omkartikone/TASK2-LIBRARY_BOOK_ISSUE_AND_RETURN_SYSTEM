module com.codeclausetask2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires transitive java.sql;
    
    opens com.codeclausetask2 to javafx.fxml;
    exports com.codeclausetask2;
}
