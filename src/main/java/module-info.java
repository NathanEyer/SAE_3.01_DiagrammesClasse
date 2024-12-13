module diagramme.diag {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.management;


    opens diagramme.diag to javafx.fxml;
    exports diagramme.diag;
    exports diagramme.diag.classe;
    opens diagramme.diag.classe to javafx.fxml;
}