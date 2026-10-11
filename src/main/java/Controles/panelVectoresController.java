package Controles;

import Modelos.PlanoCartesiano.Vector2D;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class panelVectoresController {

    @FXML private TextField txtX;
    @FXML private TextField txtY;
    @FXML private TextField txtEscalar;
    @FXML private TextField txtSumar;
    @FXML private TextField txtRestar;
    @FXML private TextField txtPPunto;
    @FXML private TextField txtAngulo;
    @FXML private TextField txtProyeccion;
    @FXML private TextField txtMagnitud;
    @FXML private TextField txtNormalizar;
    @FXML private TextField txtMultEscalar;
    @FXML private ComboBox<Vector2D> cboxVectorA;
    @FXML private ComboBox<Vector2D> cBoxVectorB;

    private planoCartesianoController plano;
    private ObservableList<Vector2D> listaVectores;
    private TextArea consola;

    public void configurar(planoCartesianoController plano, ObservableList<Vector2D> listaVectores, TextArea consola) {
        this.plano = plano;
        this.listaVectores = listaVectores;
        this.consola = consola;
        cboxVectorA.setItems(listaVectores);
        cBoxVectorB.setItems(listaVectores);
    }

    @FXML
    private void handleCrearVector() {
        try {
            double x = Double.parseDouble(txtX.getText().trim());
            double y = Double.parseDouble(txtY.getText().trim());
            if (!Double.isFinite(x) || !Double.isFinite(y)) {
                consola.appendText("Error: Los componentes deben ser números finitos.\n");
                return;
            }
            plano.agregarVector2D(x, y);
            Vector2D vector = new Vector2D(x, y);
            listaVectores.add(vector);
            txtX.clear();
            txtY.clear();
            if (cboxVectorA.getValue() == null) cboxVectorA.setValue(vector);
            if (cBoxVectorB.getValue() == null) cBoxVectorB.setValue(vector);
        } catch (NumberFormatException e) {
            consola.appendText("Error: Ingresa componentes numéricos válidos.\n");
        }
    }

    @FXML
    private void handleSumar() {
        Vector2D vA = cboxVectorA.getValue();
        Vector2D vB = cBoxVectorB.getValue();
        if (validarSeleccion(vA, vB)) {
            txtSumar.setText(vA.sumar(vB).toString());
        }
    }

    @FXML
    private void handleRestar() {
        Vector2D vA = cboxVectorA.getValue();
        Vector2D vB = cBoxVectorB.getValue();
        if (validarSeleccion(vA, vB)) {
            txtRestar.setText(vA.restar(vB).toString());
        }
    }

    @FXML
    private void handleProductoPunto() {
        Vector2D vA = cboxVectorA.getValue();
        Vector2D vB = cBoxVectorB.getValue();
        if (validarSeleccion(vA, vB)) {
            txtPPunto.setText(String.format("%.4f", vA.obtenerProductoPunto(vB)));
        }
    }

    @FXML
    private void handleAngulo() {
        Vector2D vA = cboxVectorA.getValue();
        Vector2D vB = cBoxVectorB.getValue();
        if (validarSeleccion(vA, vB)) {
            if (vA.obtenerMagnitud() == 0 || vB.obtenerMagnitud() == 0) {
                consola.appendText("Error: El ángulo no está definido para un vector nulo.\n");
                return;
            }
            txtAngulo.setText(String.format("%.4f°", Math.toDegrees(vA.obtenerAngulo(vA, vB))));
        }
    }

    @FXML
    private void handleProyeccion() {
        Vector2D vA = cboxVectorA.getValue();
        Vector2D vB = cBoxVectorB.getValue();
        if (validarSeleccion(vA, vB)) {
            if (vB.obtenerMagnitud() == 0) {
                consola.appendText("Error: No se puede proyectar sobre un vector nulo.\n");
                return;
            }
            txtProyeccion.setText(vA.obtenerProyeccion(vA, vB).toString());
        }
    }

    @FXML
    private void handleMagnitud() {
        Vector2D vA = cboxVectorA.getValue();
        if (validarSeleccionIndividual(vA)) {
            txtMagnitud.setText(String.format("%.4f", vA.obtenerMagnitud()));
        }
    }

    @FXML
    private void handleNormalizar() {
        Vector2D vA = cboxVectorA.getValue();
        if (validarSeleccionIndividual(vA)) {
            try {
                txtNormalizar.setText(vA.normalizar().toString());
            } catch (ArithmeticException e) {
                consola.appendText("Error: " + e.getMessage() + ".\n");
            }
        }
    }

    @FXML
    private void handleMultEscalar() {
        Vector2D vA = cboxVectorA.getValue();
        if (validarSeleccionIndividual(vA)) {
            try {
                double k = Double.parseDouble(txtEscalar.getText().trim());
                txtMultEscalar.setText(vA.multEscalar(k).toString());
            } catch (NumberFormatException e) {
                consola.appendText("Error: Ingresa un escalar numérico válido.\n");
            }
        }
    }

    private boolean validarSeleccion(Vector2D vA, Vector2D vB) {
        if (vA == null || vB == null) {
            consola.appendText("Error: Selecciona dos vectores de las listas.\n");
            return false;
        }
        return true;
    }

    private boolean validarSeleccionIndividual(Vector2D v) {
        if (v == null) {
            consola.appendText("Error: Selecciona un vector en la lista Vector A.\n");
            return false;
        }
        return true;
    }
}
