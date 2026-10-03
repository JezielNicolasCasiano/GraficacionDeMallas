module jeziel.graficaciondemallas {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens jeziel.graficaciondemallas to javafx.fxml;
    opens Controles to javafx.fxml;
    exports jeziel.graficaciondemallas;
}