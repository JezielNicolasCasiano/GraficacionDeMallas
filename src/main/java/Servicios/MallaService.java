package Servicios;
import Modelos.Malla;
import Modelos.Poligono;
import Modelos.Punto;
public class MallaService {
    private final Malla malla;

    public MallaService(Malla malla) {
        this.malla = malla;
    }

    public double calcularAreaTotal() {
        double areaTotal = 0;
        for (Poligono poligono : malla.getPoligonos()) {
            areaTotal += poligono.calcularArea(malla.getVertices());
        }
        return areaTotal;
    }

    public double calcularPerimetroTotal() {
        double perimetroTotal = 0;
        for (Poligono poligono : malla.getPoligonos()) {
            perimetroTotal += poligono.calcularPerimetro(malla.getVertices());
        }
        return perimetroTotal;
    }
}
