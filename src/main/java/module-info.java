module org.example.csc311progresscheckassignment {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.csc311progresscheckassignment to javafx.fxml;
    exports org.example.csc311progresscheckassignment;
}