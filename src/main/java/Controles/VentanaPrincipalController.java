package Controles;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import java.io.IOException;
public class VentanaPrincipalController {

    @FXML private VBox contenedorMallas; // Debe coincidir con el fx:id de la vista principal

    @FXML
    public void initialize() {
        cargarPanelMallas();
    }

    private void cargarPanelMallas() {
        try {
            // Cargar el archivo FXML correspondiente a la malla
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/jeziel/graficaciondemallas/panelMallas.fxml"));
            Parent vistaMallas = loader.load();

            // Limpiar contenido previo y agregar la vista cargada
            contenedorMallas.getChildren().clear();
            contenedorMallas.getChildren().add(vistaMallas);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar panelMallas.fxml: Verifica la ruta del archivo FXML.");
        }
    }

}
