module diagramme.diag {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.management;


    opens diagrammes to javafx.fxml;
    exports diagrammes;
    exports diagrammes.classe;
    opens diagrammes.classe to javafx.fxml;
}