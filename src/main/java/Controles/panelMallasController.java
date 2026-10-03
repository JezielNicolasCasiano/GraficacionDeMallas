package Controles;


import Modelos.FilaMalla;
import Modelos.Malla;
import Modelos.Poligono;
import Modelos.Punto;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


import java.util.Arrays;
import java.util.List;

public class panelMallasController {

        private Malla malla;
        private ObservableList<FilaMalla> listaFilasTabla;

        // Componentes FXML
        @FXML private TableView<FilaMalla> tablaPoligonos;
        @FXML private TableColumn<FilaMalla, Integer> colIdPoligono;
        @FXML private TableColumn<FilaMalla, String> colPuntos;
        @FXML private TableColumn<FilaMalla, Double> colArea;
        @FXML private TableColumn<FilaMalla, Double> colPerimetro;

        @FXML private TextArea txtResultados;

        // Entradas de texto
        @FXML private TextField txtX;
        @FXML private TextField txtY;
        @FXML private TextField txtIndices;
        @FXML private TextField txtNumCara;
        @FXML private TextField txtIdEliminar;

        @FXML
        public void initialize() {
            this.malla = new Malla();
            this.listaFilasTabla = FXCollections.observableArrayList();

            colIdPoligono.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getIdPoligono()));
            colPuntos.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getPuntos()));
            colArea.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getArea()));
            colPerimetro.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getPerimetro()));

            tablaPoligonos.setItems(listaFilasTabla);
            txtResultados.setText("Sistema listo.\n");
        }

        // Agregar Vértice
        @FXML
        private void handleAgregarVertice(ActionEvent event) {
            try {
                double x = Double.parseDouble(txtX.getText().trim());
                double y = Double.parseDouble(txtY.getText().trim());

                malla.agregarVertice(x, y);
                txtResultados.appendText(String.format("Vértice [%d] agregado: (%.2f, %.2f)\n",
                        malla.getVertices().size() - 1, x, y));

                txtX.clear();
                txtY.clear();
            } catch (NumberFormatException e) {
                txtResultados.appendText("Error: Ingresa coordenadas numéricas válidas.\n");
            }
        }

        // Crear Polígono
        @FXML
        private void handleCrearPoligono(ActionEvent event) {
            String input = txtIndices.getText().trim();
            if (input.isEmpty()) {
                txtResultados.appendText("Error: Especifica los índices de vértices.\n");
                return;
            }

            try {
                int[] indices = Arrays.stream(input.split(","))
                        .map(String::trim)
                        .mapToInt(Integer::parseInt)
                        .toArray();

                int totalVertices = malla.getVertices().size();
                for (int idx : indices) {
                    if (idx < 0 || idx >= totalVertices) {
                        txtResultados.appendText("Error: El índice " + idx + " no existe.\n");
                        return;
                    }
                }

                malla.agregarPoligono(indices);
                txtIndices.clear();
                actualizarTabla();
                txtResultados.appendText("Polígono creado con éxito.\n");
            } catch (NumberFormatException e) {
                txtResultados.appendText("Error: Formato de índices inválido.\n");
            }
        }

        // Función Separada: Eliminar Polígono por ID
        @FXML
        private void handleEliminarPoligono(ActionEvent event) {
            try {
                int id = Integer.parseInt(txtIdEliminar.getText().trim());
                if (malla.eliminarPoligono(id)) {
                    txtResultados.appendText("Polígono con ID " + id + " eliminado.\n");
                    actualizarTabla();
                    txtIdEliminar.clear();
                } else {
                    txtResultados.appendText("Error: No existe el polígono con ID " + id + ".\n");
                }
            } catch (NumberFormatException e) {
                txtResultados.appendText("Error: Ingresa un ID numérico en el campo eliminar.\n");
            }
        }

        // Función Separada: Eliminar Vértice por ID
        @FXML
        private void handleEliminarVertice(ActionEvent event) {
            try {
                int id = Integer.parseInt(txtIdEliminar.getText().trim());
                if (malla.eliminarVertice(id)) {
                    txtResultados.appendText("Vértice con ID " + id + " eliminado y referencias reajustadas.\n");
                    actualizarTabla();
                    txtIdEliminar.clear();
                } else {
                    txtResultados.appendText("Error: No existe el vértice con ID " + id + ".\n");
                }
            } catch (NumberFormatException e) {
                txtResultados.appendText("Error: Ingresa un ID numérico en el campo eliminar.\n");
            }
        }

        // Consultar Cara
        @FXML
        private void handleConsultarCara(ActionEvent event) {
            try {
                int numCara = Integer.parseInt(txtNumCara.getText().trim());
                Punto[] puntosCara = malla.obtenerPuntosDeCara(numCara);

                if (puntosCara.length == 0) {
                    txtResultados.appendText("Error: No se encontró la cara #" + numCara + "\n");
                    return;
                }

                Poligono pol = malla.getPoligonos().get(numCara);
                double area = pol.calcularArea(malla.getVertices());
                double perimetro = pol.calcularPerimetro(malla.getVertices());

                StringBuilder sb = new StringBuilder();
                sb.append("--- DETALLE DE POLÍGONO ID ").append(numCara).append(" ---\n");
                sb.append("Vértices: ");
                for (Punto p : puntosCara) {
                    sb.append(String.format("(%.1f, %.1f) ", p.getX(), p.getY()));
                }
                sb.append(String.format("\nÁrea: %.2f | Perímetro: %.2f\n", area, perimetro));

                txtResultados.appendText(sb.toString());
            } catch (NumberFormatException e) {
                txtResultados.appendText("Error: Ingresa un ID numérico válido para la cara.\n");
            }
        }

        // Calcular Área y Perímetro Total de Malla
        @FXML
        private void handleCalcularMalla(ActionEvent event) {
            List<Poligono> poligonos = malla.getPoligonos();
            List<Punto> vertices = malla.getVertices();

            if (poligonos.isEmpty()) {
                txtResultados.appendText("La malla no posee polígonos.\n");
                return;
            }

            double areaTotal = 0;
            double perimetroTotal = 0;

            for (Poligono p : poligonos) {
                areaTotal += p.calcularArea(vertices);
                perimetroTotal += p.calcularPerimetro(vertices);
            }

            txtResultados.appendText("\n=== TOTALES Malla ===\n");
            txtResultados.appendText("Vértices Totales: " + vertices.size() + "\n");
            txtResultados.appendText("Polígonos Totales: " + poligonos.size() + "\n");
            txtResultados.appendText(String.format("Área Total: %.2f\n", areaTotal));
            txtResultados.appendText(String.format("Perímetro Total: %.2f\n", perimetroTotal));
            txtResultados.appendText("=====================\n");
        }

        @FXML
        private void handleLimpiar(ActionEvent event) {
            this.malla = new Malla();
            listaFilasTabla.clear();
            txtResultados.setText("Estado reiniciado.\n");
        }

        @FXML
        private void handleActualizar(ActionEvent event) {
            actualizarTabla();
            txtResultados.appendText("Tabla actualizada.\n");
        }

        private void actualizarTabla() {
            listaFilasTabla.clear();
            List<Poligono> poligonos = malla.getPoligonos();
            List<Punto> vertices = malla.getVertices();

            for (int i = 0; i < poligonos.size(); i++) {
                Poligono pol = poligonos.get(i);
                Punto[] puntosCara = malla.obtenerPuntosDeCara(i);

                StringBuilder puntosStr = new StringBuilder();
                for (Punto p : puntosCara) {
                    if (p != null) {
                        puntosStr.append(String.format("(%.1f, %.1f) ", p.getX(), p.getY()));
                    }
                }

                double area = pol.calcularArea(vertices);
                double perimetro = pol.calcularPerimetro(vertices);

                listaFilasTabla.add(new FilaMalla(i, puntosStr.toString(), area, perimetro));
            }
        }
    }

