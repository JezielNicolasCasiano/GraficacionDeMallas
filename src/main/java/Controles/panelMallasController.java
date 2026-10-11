package Controles;


import Modelos.FilaMalla;
import Modelos.Malla;
import Modelos.Poligono;
import Modelos.PlanoCartesiano.Punto2D;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;


import java.util.ArrayList;
import java.util.List;

public class panelMallasController {

        private Malla malla;
        private planoCartesianoController plano;
        private TextArea txtResultados;
        private ObservableList<Punto2D> listaPuntos;
        private final ObservableList<FilaMalla> listaFilasTabla = FXCollections.observableArrayList();
        private final List<Integer> indicesEnConstruccion = new ArrayList<>();

        @FXML private TableView<Punto2D> tablaPuntos;
        @FXML private TableColumn<Punto2D, String> colIdPunto;
        @FXML private TableColumn<Punto2D, Double> colX;
        @FXML private TableColumn<Punto2D, Double> colY;

        @FXML private TableView<FilaMalla> tablaPoligonos;
        @FXML private TableColumn<FilaMalla, Integer> colIdPoligono;
        @FXML private TableColumn<FilaMalla, String> colPuntos;

        @FXML private ComboBox<Punto2D> cBoxPunto;
        @FXML private ComboBox<Integer> cBoxPoligono;
        @FXML private Label lblConstruccion;

        @FXML
        public void initialize() {
            colIdPoligono.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getIdPoligono()));
            colPuntos.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getPuntos()));
            colIdPunto.setCellValueFactory(cell -> new ReadOnlyStringWrapper("P" + listaPuntos.indexOf(cell.getValue())));
            colX.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getOrdenadaX()));
            colY.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getOrdenadaY()));
            tablaPoligonos.setItems(listaFilasTabla);

            tablaPoligonos.getSelectionModel().selectedItemProperty().addListener((o, anterior, fila) -> {
                if (fila != null) {
                    cBoxPoligono.setValue(fila.getIdPoligono());
                }
            });
            cBoxPoligono.valueProperty().addListener((o, anterior, id) -> {
                if (id == null) {
                    tablaPoligonos.getSelectionModel().clearSelection();
                } else {
                    tablaPoligonos.getSelectionModel().select(id.intValue());
                }
            });
        }

        public void configurar(planoCartesianoController plano, Malla malla, ObservableList<Punto2D> listaPuntos, TextArea consola) {
            this.plano = plano;
            this.malla = malla;
            this.listaPuntos = listaPuntos;
            this.txtResultados = consola;
            tablaPuntos.setItems(listaPuntos);
            cBoxPunto.setItems(listaPuntos);
            cBoxPunto.setConverter(panelPuntosController.convertidorPuntos(listaPuntos));
            cBoxPunto.valueProperty().addListener((o, anterior, actual) -> resaltarSeleccion());
            actualizarTabla();
            actualizarConstruccion();
        }

        public void resaltarSeleccion() {
            Punto2D punto = cBoxPunto.getValue();
            plano.setPuntosSeleccionados(punto == null ? List.of() : List.of(listaPuntos.indexOf(punto)));
        }

        @FXML
        private void handleAgregarPunto() {
            Punto2D punto = cBoxPunto.getValue();
            if (punto == null) {
                txtResultados.appendText("Error: Selecciona un punto.\n");
                return;
            }
            int indice = listaPuntos.indexOf(punto);
            if (indicesEnConstruccion.contains(indice)) {
                txtResultados.appendText("Error: El punto P" + indice + " ya está en el polígono.\n");
                return;
            }
            indicesEnConstruccion.add(indice);
            actualizarConstruccion();
        }

        @FXML
        private void handleQuitarUltimo() {
            if (indicesEnConstruccion.isEmpty()) {
                txtResultados.appendText("Error: El polígono en construcción está vacío.\n");
                return;
            }
            indicesEnConstruccion.remove(indicesEnConstruccion.size() - 1);
            actualizarConstruccion();
        }

        @FXML
        private void handleCrearPoligono() {
            int[] indices = indicesEnConstruccion.stream().mapToInt(Integer::intValue).toArray();
            try {
                malla.agregarPoligono(indices);
                indicesEnConstruccion.clear();
                actualizarConstruccion();
                actualizarTabla();
            } catch (IllegalArgumentException e) {
                txtResultados.appendText("Error: " + e.getMessage() + "\n");
            }
        }

        @FXML
        private void handleArea() {
            Poligono poligono = poligonoSeleccionado();
            if (poligono != null) {
                txtResultados.appendText(String.format("Área: %.2f\n", poligono.calcularArea(malla.getVertices())));
            }
        }

        @FXML
        private void handlePerimetro() {
            Poligono poligono = poligonoSeleccionado();
            if (poligono != null) {
                txtResultados.appendText(String.format("Perímetro: %.2f\n", poligono.calcularPerimetro(malla.getVertices())));
            }
        }

        @FXML
        private void handleEliminarPoligono() {
            Integer id = cBoxPoligono.getValue();
            if (id == null) {
                txtResultados.appendText("Error: Selecciona el ID del polígono.\n");
                return;
            }
            malla.eliminarPoligono(id);
            actualizarTabla();
        }

        @FXML
        private void handleReiniciar() {
            malla.eliminarPoligonos();
            actualizarTabla();
        }

        private Poligono poligonoSeleccionado() {
            Integer id = cBoxPoligono.getValue();
            if (id == null) {
                txtResultados.appendText("Error: Selecciona el ID del polígono.\n");
                return null;
            }
            return malla.getPoligonos().get(id);
        }

        private void actualizarConstruccion() {
            StringBuilder texto = new StringBuilder("Polígono en construcción: ");
            if (indicesEnConstruccion.isEmpty()) {
                texto.append("(vacío)");
            }
            for (int i = 0; i < indicesEnConstruccion.size(); i++) {
                texto.append(i == 0 ? "" : " → ").append("P").append(indicesEnConstruccion.get(i));
            }
            lblConstruccion.setText(texto.toString());
        }

        private void actualizarTabla() {
            listaFilasTabla.clear();
            List<Integer> ids = new ArrayList<>();
            List<Poligono> poligonos = malla.getPoligonos();

            for (int i = 0; i < poligonos.size(); i++) {
                int[] indices = poligonos.get(i).getIndicesVertices();
                StringBuilder puntosStr = new StringBuilder();
                for (int j = 0; j < indices.length; j++) {
                    puntosStr.append(j == 0 ? "" : ", ").append("P").append(indices[j]);
                }
                listaFilasTabla.add(new FilaMalla(i, puntosStr.toString()));
                ids.add(i);
            }
            cBoxPoligono.getItems().setAll(ids);
            cBoxPoligono.setValue(null);
            plano.limpiarTriangulos();
        }
    }
