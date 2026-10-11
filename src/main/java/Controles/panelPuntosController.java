package Controles;

import Modelos.Malla;
import Modelos.PlanoCartesiano.Punto2D;
import Modelos.PlanoCartesiano.Vector2D;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class panelPuntosController {

    @FXML private TextField txtPX;
    @FXML private TextField txtPY;
    @FXML private TextField txtDistancia;
    @FXML private TextField txtVectorAPunto;
    @FXML private TextField txtTrasladar;
    @FXML private ComboBox<Punto2D> cBoxPuntoA;
    @FXML private ComboBox<Punto2D> cBoxPuntoB;
    @FXML private ComboBox<Vector2D> cBoxVector;

    private planoCartesianoController plano;
    private Malla malla;
    private ObservableList<Punto2D> listaPuntos;
    private TextArea consola;

    public void configurar(planoCartesianoController plano, Malla malla, ObservableList<Punto2D> listaPuntos,
                           ObservableList<Vector2D> listaVectores, TextArea consola) {
        this.plano = plano;
        this.malla = malla;
        this.listaPuntos = listaPuntos;
        this.consola = consola;
        cBoxPuntoA.setItems(listaPuntos);
        cBoxPuntoB.setItems(listaPuntos);
        cBoxVector.setItems(listaVectores);
        cBoxPuntoA.setConverter(convertidorPuntos(listaPuntos));
        cBoxPuntoB.setConverter(convertidorPuntos(listaPuntos));
        cBoxPuntoA.valueProperty().addListener((o, anterior, actual) -> resaltarSeleccion());
        cBoxPuntoB.valueProperty().addListener((o, anterior, actual) -> resaltarSeleccion());
    }

    public void resaltarSeleccion() {
        List<Integer> seleccionados = new ArrayList<>();
        if (cBoxPuntoA.getValue() != null) seleccionados.add(listaPuntos.indexOf(cBoxPuntoA.getValue()));
        if (cBoxPuntoB.getValue() != null) seleccionados.add(listaPuntos.indexOf(cBoxPuntoB.getValue()));
        plano.setPuntosSeleccionados(seleccionados);
    }

    public static StringConverter<Punto2D> convertidorPuntos(List<Punto2D> listaPuntos) {
        return new StringConverter<>() {
            @Override
            public String toString(Punto2D punto) {
                if (punto == null) return "";
                return String.format(Locale.US, "P%d (%.2f, %.2f)", listaPuntos.indexOf(punto),
                        punto.getOrdenadaX(), punto.getOrdenadaY());
            }

            @Override
            public Punto2D fromString(String texto) {
                return null;
            }
        };
    }

    @FXML
    private void handleCrearPunto() {
        try {
            double x = Double.parseDouble(txtPX.getText().trim());
            double y = Double.parseDouble(txtPY.getText().trim());
            malla.agregarVertice(x, y);
            Punto2D punto = new Punto2D(x, y);
            listaPuntos.add(punto);
            plano.redibujar();
            txtPX.clear();
            txtPY.clear();
            if (cBoxPuntoA.getValue() == null) cBoxPuntoA.setValue(punto);
            if (cBoxPuntoB.getValue() == null) cBoxPuntoB.setValue(punto);
        } catch (NumberFormatException e) {
            consola.appendText("Error: Ingresa coordenadas numéricas válidas.\n");
        } catch (IllegalArgumentException e) {
            consola.appendText("Error: " + e.getMessage() + "\n");
        }
    }

    @FXML
    private void handleCalcularDistancia() {
        Punto2D pA = cBoxPuntoA.getValue();
        Punto2D pB = cBoxPuntoB.getValue();
        if (validarPuntos(pA, pB)) {
            txtDistancia.setText(String.format("%.4f", pA.obtenerDistanciaPunto(pB)));
        }
    }

    @FXML
    private void handleVectorHastaPunto() {
        Punto2D pA = cBoxPuntoA.getValue();
        Punto2D pB = cBoxPuntoB.getValue();
        if (validarPuntos(pA, pB)) {
            txtVectorAPunto.setText(pA.vectorHastaPunto(pB).toString());
        }
    }

    @FXML
    private void handleTrasladarPunto() {
        Punto2D pA = cBoxPuntoA.getValue();
        Vector2D v = cBoxVector.getValue();
        if (pA == null || v == null) {
            consola.appendText("Error: Selecciona el punto A y un vector.\n");
            return;
        }
        txtTrasladar.setText(pA.trasladarPunto(v).toString());
    }

    private boolean validarPuntos(Punto2D pA, Punto2D pB) {
        if (pA == null || pB == null) {
            consola.appendText("Error: Selecciona dos puntos de las listas.\n");
            return false;
        }
        return true;
    }
}
