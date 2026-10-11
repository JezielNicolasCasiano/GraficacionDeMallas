package Controles;

import Modelos.FilaTriangulo;
import Modelos.Malla;
import Modelos.Poligono;
import Modelos.PlanoCartesiano.Punto2D;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.List;

public class panelTriangulacionController {

    private static final String TODOS = "Todos los polígonos";

    @FXML private ComboBox<String> cBoxPoligono;
    @FXML private TableView<Punto2D> tablaPuntos;
    @FXML private TableColumn<Punto2D, String> colIdPunto;
    @FXML private TableColumn<Punto2D, Double> colX;
    @FXML private TableColumn<Punto2D, Double> colY;
    @FXML private TableView<FilaTriangulo> tablaTriangulos;
    @FXML private TableColumn<FilaTriangulo, String> colOrden;
    @FXML private TableColumn<FilaTriangulo, Integer> colPoligono;
    @FXML private TableColumn<FilaTriangulo, String> colVertices;

    private final ObservableList<FilaTriangulo> listaTriangulos = FXCollections.observableArrayList();
    private planoCartesianoController plano;
    private Malla malla;
    private ObservableList<Punto2D> listaPuntos;
    private TextArea consola;

    @FXML
    public void initialize() {
        colOrden.setCellValueFactory(cell -> new ReadOnlyStringWrapper("T" + cell.getValue().getOrden()));
        colPoligono.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getIdPoligono()));
        colVertices.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getVertices()));
        colIdPunto.setCellValueFactory(cell -> new ReadOnlyStringWrapper("P" + listaPuntos.indexOf(cell.getValue())));
        colX.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getOrdenadaX()));
        colY.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getOrdenadaY()));
        tablaTriangulos.setItems(listaTriangulos);
    }

    public void configurar(planoCartesianoController plano, Malla malla, ObservableList<Punto2D> listaPuntos, TextArea consola) {
        this.plano = plano;
        this.malla = malla;
        this.listaPuntos = listaPuntos;
        tablaPuntos.setItems(listaPuntos);
        this.consola = consola;
        actualizarPoligonos();
    }

    public void actualizarPoligonos() {
        String seleccion = cBoxPoligono.getValue();
        List<String> opciones = new ArrayList<>();
        opciones.add(TODOS);
        for (int i = 0; i < malla.getPoligonos().size(); i++) {
            opciones.add("Polígono " + i);
        }
        cBoxPoligono.getItems().setAll(opciones);
        cBoxPoligono.setValue(opciones.contains(seleccion) ? seleccion : TODOS);
        if (!plano.tieneTriangulos()) {
            listaTriangulos.clear();
        }
    }

    @FXML
    private void handleTriangular() {
        if (malla.getPoligonos().isEmpty()) {
            consola.appendText("Error: La malla no tiene polígonos para triangular.\n");
            return;
        }
        int seleccion = cBoxPoligono.getSelectionModel().getSelectedIndex();
        List<Integer> idsPoligonos = new ArrayList<>();
        if (seleccion <= 0) {
            for (int i = 0; i < malla.getPoligonos().size(); i++) {
                idsPoligonos.add(i);
            }
        } else {
            idsPoligonos.add(seleccion - 1);
        }

        List<Poligono> todosLosTriangulos = new ArrayList<>();
        listaTriangulos.clear();
        double sumaAreas = 0;
        try {
            for (int id : idsPoligonos) {
                for (Poligono triangulo : malla.triangularPoligono(id)) {
                    sumaAreas += triangulo.calcularArea(malla.getVertices());
                    todosLosTriangulos.add(triangulo);
                    listaTriangulos.add(new FilaTriangulo(todosLosTriangulos.size(), id, describir(triangulo)));
                }
            }
            consola.appendText("Triángulos: " + todosLosTriangulos.size() + "\n");
            consola.appendText(String.format("Suma de áreas: %.2f\n", sumaAreas));
        } catch (IllegalArgumentException | IllegalStateException e) {
            consola.appendText("Error: " + e.getMessage() + "\n");
        }
        plano.setTriangulos(todosLosTriangulos);
    }

    @FXML
    private void handleLimpiarTriangulacion() {
        listaTriangulos.clear();
        plano.limpiarTriangulos();
    }

    private static String describir(Poligono triangulo) {
        int[] indices = triangulo.getIndicesVertices();
        return "P" + indices[0] + ", P" + indices[1] + ", P" + indices[2];
    }
}
