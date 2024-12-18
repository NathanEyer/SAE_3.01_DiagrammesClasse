module diagrammes.diag {
    requires javafx.controls;
    requires javafx.fxml;

    exports diagrammes;


    opens diagrammes to javafx.fxml;

}