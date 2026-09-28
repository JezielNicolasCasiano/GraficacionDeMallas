package Modelos;
import java.util.ArrayList;
import java.util.List;

public class Malla {

    private List<Poligono> poligonos;
    private List<Punto> vertices;

    public Malla() {
        this.poligonos = new ArrayList<>();
        this.vertices = new ArrayList<>();
    }


    public void agregarVertice(double x, double y) {
        this.vertices.add(new Punto(x, y));
    }

    public void agregarPoligono(int... indices) {
        this.poligonos.add(new Poligono(indices));
    }

    public Punto[] obtenerPuntosDeCara(int numCara) {
        if (numCara < 0 || numCara >= poligonos.size()) {
            return new Punto[0];
        }

        Poligono poligono = poligonos.get(numCara);
        int[] indices = poligono.getIndicesVertices();
        Punto[] puntosCara = new Punto[indices.length];

        for (int i = 0; i < indices.length; i++) {
            int idx = indices[i];
            if (idx >= 0 && idx < vertices.size()) {
                puntosCara[i] = vertices.get(idx);
            }
        }

        return puntosCara;
    }

    public List<Poligono> getPoligonos() {
        return poligonos;
    }

    public List<Punto> getVertices() {
        return vertices;
    }
}
