package Controles;

import Modelos.FilaMalla;
import Modelos.Malla;
import Modelos.Poligono;
import Modelos.Punto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;


import java.util.Arrays;
import java.util.List;

public class panelMallasController {
    private Malla malla;
    private ObservableList<FilaMalla> listaFilasTabla;

    // Componentes FXML - Tabla y Consola
    @FXML private TableView<FilaMalla> tablaPoligonos;
    @FXML private TableColumn<FilaMalla, Integer> colIdPoligono;
    @FXML private TableColumn<FilaMalla, String> colPuntos;
    @FXML private TableColumn<FilaMalla, Double> colArea;
    @FXML private TableColumn<FilaMalla, Double> colPerimetro;

    @FXML private TextArea txtResultados;

    // Inputs
    @FXML private TextField txtX;
    @FXML private TextField txtY;
    @FXML private TextField txtIndices;
    @FXML private TextField txtNumCara;

    @FXML
    public void initialize() {
        this.malla = new Malla();
        this.listaFilasTabla = FXCollections.observableArrayList();

        // Configuración de las columnas de la tabla con la clase FilaMalla
        colIdPoligono.setCellValueFactory(new PropertyValueFactory<>("idPoligono"));
        colPuntos.setCellValueFactory(new PropertyValueFactory<>("puntos"));
        colArea.setCellValueFactory(new PropertyValueFactory<>("area"));
        colPerimetro.setCellValueFactory(new PropertyValueFactory<>("perimetro"));

        tablaPoligonos.setItems(listaFilasTabla);
        txtResultados.setText("Sistema listo. Registra vértices y polígonos.\n");
    }

    // 1. Agregar Vértice
    @FXML
    private void handleAgregarVertice(ActionEvent event) {
        try {
            double x = Double.parseDouble(txtX.getText().trim());
            double y = Double.parseDouble(txtY.getText().trim());

            malla.agregarVertice(x, y);

            txtResultados.appendText(String.format("Vértice [%d] registrado: (%.2f, %.2f)\n",
                    malla.getVertices().size() - 1, x, y));

            txtX.clear();
            txtY.clear();

        } catch (NumberFormatException e) {
            txtResultados.appendText("Error: Ingresa coordenadas X e Y válidas.\n");
        }
    }

    // 2. Crear Polígono
    @FXML
    private void handleCrearPoligono(ActionEvent event) {
        String input = txtIndices.getText().trim();
        if (input.isEmpty()) {
            txtResultados.appendText("Error: Ingresa los índices de los vértices (ej: 0, 1, 2).\n");
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
                    txtResultados.appendText("Error: Índice " + idx + " inexistente. Vértices disponibles: 0 a " + (totalVertices - 1) + "\n");
                    return;
                }
            }

            malla.agregarPoligono(indices);
            txtIndices.clear();

            // Actualiza la tabla automáticamente con el nuevo polígono
            actualizarTabla();
            txtResultados.appendText("Polígono creado con éxito.\n");

        } catch (NumberFormatException e) {
            txtResultados.appendText("Error: Formato de índices no válido.\n");
        }
    }

    // 3. Consultar Cara por ID
    @FXML
    private void handleConsultarCara(ActionEvent event) {
        try {
            int numCara = Integer.parseInt(txtNumCara.getText().trim());
            Punto[] puntosCara = malla.obtenerPuntosDeCara(numCara);

            if (puntosCara.length == 0) {
                txtResultados.appendText("Error: No existe el polígono/cara #" + numCara + "\n");
                return;
            }

            Poligono pol = malla.getPoligonos().get(numCara);
            double area = pol.calcularArea(malla.getVertices());
            double perimetro = pol.calcularPerimetro(malla.getVertices());

            StringBuilder sb = new StringBuilder();
            sb.append("--- CONSULTA CARA ID ").append(numCara).append(" ---\n");
            sb.append("Vértices: ");
            for (Punto p : puntosCara) {
                sb.append(String.format("(%.1f, %.1f) ", p.getX(), p.getY()));
            }
            sb.append(String.format("\nÁrea: %.2f | Perímetro: %.2f\n", area, perimetro));

            txtResultados.appendText(sb.toString());

        } catch (NumberFormatException e) {
            txtResultados.appendText("Error: Ingresa un ID numérico para la cara.\n");
        }
    }

    // 4. Calcular Métricas Globales
    @FXML
    private void handleCalcularMalla(ActionEvent event) {
        List<Poligono> poligonos = malla.getPoligonos();
        List<Punto> vertices = malla.getVertices();

        if (poligonos.isEmpty()) {
            txtResultados.appendText("La malla no tiene polígonos registrados.\n");
            return;
        }

        double areaTotal = 0;
        double perimetroTotal = 0;

        for (Poligono p : poligonos) {
            areaTotal += p.calcularArea(vertices);
            perimetroTotal += p.calcularPerimetro(vertices);
        }

        txtResultados.appendText("\n=== RESUMEN DE LA MALLA ===\n");
        txtResultados.appendText("Total Vértices: " + vertices.size() + "\n");
        txtResultados.appendText("Total Polígonos: " + poligonos.size() + "\n");
        txtResultados.appendText(String.format("Área Total: %.2f\n", areaTotal));
        txtResultados.appendText(String.format("Perímetro Total: %.2f\n", perimetroTotal));
        txtResultados.appendText("===========================\n");
    }

    @FXML
    private void handleLimpiar(ActionEvent event) {
        this.malla = new Malla();
        listaFilasTabla.clear();
        txtResultados.setText("Malla y tabla reiniciadas.\n");
    }

    @FXML
    private void handleActualizar(ActionEvent event) {
        actualizarTabla();
        txtResultados.appendText("Tabla actualizada.\n");
    }

    // Método que actualiza las filas de la TableView leyendo directamente el modelo
    private void actualizarTabla() {
        listaFilasTabla.clear();
        List<Poligono> poligonos = malla.getPoligonos();
        List<Punto> vertices = malla.getVertices();

        for (int i = 0; i < poligonos.size(); i++) {
            Poligono pol = poligonos.get(i);
            Punto[] puntosCara = malla.obtenerPuntosDeCara(i);

            // Construir representación textual de las coordenadas
            StringBuilder puntosStr = new StringBuilder();
            for (Punto p : puntosCara) {
                puntosStr.append(String.format("(%.1f, %.1f) ", p.getX(), p.getY()));
            }

            double area = pol.calcularArea(vertices);
            double perimetro = pol.calcularPerimetro(vertices);

            listaFilasTabla.add(new FilaMalla(i, puntosStr.toString(), area, perimetro));
        }
    }
}
