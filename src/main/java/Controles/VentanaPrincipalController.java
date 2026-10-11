package Controles;

import Modelos.Malla;
import Modelos.PlanoCartesiano.Punto2D;
import Modelos.PlanoCartesiano.Vector2D;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;

import java.util.List;

public class VentanaPrincipalController {

    @FXML private planoCartesianoController planoCartesianoController;
    @FXML private panelPuntosController panelPuntosController;
    @FXML private panelVectoresController panelVectoresController;
    @FXML private panelMallasController panelMallasController;
    @FXML private panelTriangulacionController panelTriangulacionController;

    @FXML private TabPane pestanas;
    @FXML private Tab tabPuntos;
    @FXML private Tab tabMallas;
    @FXML private Tab tabTriangulacion;
    @FXML private TextArea txtConsola;

    private final Malla malla = new Malla();
    private final ObservableList<Punto2D> listaPuntos = FXCollections.observableArrayList();
    private final ObservableList<Vector2D> listaVectores = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        planoCartesianoController.setMalla(malla);
        panelPuntosController.configurar(planoCartesianoController, malla, listaPuntos, listaVectores, txtConsola);
        panelVectoresController.configurar(planoCartesianoController, listaVectores, txtConsola);
        panelMallasController.configurar(planoCartesianoController, malla, listaPuntos, txtConsola);
        panelTriangulacionController.configurar(planoCartesianoController, malla, listaPuntos, txtConsola);

        pestanas.getSelectionModel().selectedItemProperty().addListener((o, anterior, actual) -> {
            if (actual == tabPuntos) {
                panelPuntosController.resaltarSeleccion();
            } else if (actual == tabMallas) {
                panelMallasController.resaltarSeleccion();
            } else {
                planoCartesianoController.setPuntosSeleccionados(List.of());
            }
            if (actual == tabTriangulacion) {
                panelTriangulacionController.actualizarPoligonos();
            }
        });
    }
}
