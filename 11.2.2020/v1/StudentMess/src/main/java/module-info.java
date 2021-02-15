module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires java.sql;

    opens org.example to javafx.fxml;
    exports org.example;
}