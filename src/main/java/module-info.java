module diagrammes.diag {
    requires javafx.controls;
    requires javafx.fxml;

    exports diagrammes;


    opens diagrammes.diag to javafx.fxml;
    exports diagrammes.diag;
}