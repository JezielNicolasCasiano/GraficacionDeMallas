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
        if (!Double.isFinite(x) || !Double.isFinite(y)) {
            throw new IllegalArgumentException("Las coordenadas deben ser números finitos.");
        }
        for (int i = 0; i < vertices.size(); i++) {
            Punto p = vertices.get(i);
            if (p.getX() == x && p.getY() == y) {
                throw new IllegalArgumentException("Ya existe el punto P" + i + " en esas coordenadas.");
            }
        }
        this.vertices.add(new Punto(x, y));
    }

    public void agregarPoligono(int... indices) {
        Poligono poligono = new Poligono(indices);
        String error = poligono.validar(vertices);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }
        for (int i = 0; i < poligonos.size(); i++) {
            if (poligono.seEnciman(poligonos.get(i), vertices)) {
                throw new IllegalArgumentException("El polígono se encima con el polígono " + i + ".");
            }
        }
        this.poligonos.add(poligono);
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

    public List<Poligono> triangularPoligono(int numCara) {
        return poligonos.get(numCara).triangular(vertices);
    }

    public void eliminarPoligonos() {
        poligonos.clear();
    }

    public List<Poligono> getPoligonos() {
        return poligonos;
    }

    public List<Punto> getVertices() {
        return vertices;
    }

    public boolean eliminarPoligono(int indice) {
        if (indice >= 0 && indice < poligonos.size()) {
            poligonos.remove(indice);
            return true;
        }
        return false;
    }

    public boolean eliminarVertice(int indice) {
        if (indice < 0 || indice >= vertices.size()) {
            return false;
        }

        vertices.remove(indice);

        for (Poligono pol : poligonos) {
            int[] indices = pol.getIndicesVertices();
            int[] nuevosIndices = java.util.Arrays.stream(indices)
                    .filter(idx -> idx != indice)
                    .map(idx -> idx > indice ? idx - 1 : idx)
                    .toArray();
            pol.setIndicesVertices(nuevosIndices);
        }
        poligonos.removeIf(pol -> pol.validar(vertices) != null);
        return true;
    }
}
