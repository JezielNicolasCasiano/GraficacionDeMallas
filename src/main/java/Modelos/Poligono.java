package Modelos;

import java.util.ArrayList;
import java.util.List;

public class Poligono {

    private static final double EPSILON = 1e-9;

    private int[] indicesVertices;

    public Poligono(int... indicesVertices){

        this.indicesVertices = indicesVertices.clone();

    }

    public Punto buscarVertice(List<Punto> vertices,int i){
        int indiceGlobal = this.indicesVertices[i];
        return vertices.get(indiceGlobal);
    }

    public void mostrar(){



    }
   public double calcularArea(List<Punto> vertices){
        return Math.abs(calcularAreaConSigno(vertices));
   }
   private double calcularAreaConSigno(List<Punto> vertices){
        double prodCruz=0;
        int n=indicesVertices.length;
        for(int i=0;i<n;i++){

            Punto p1 = buscarVertice(vertices, i);
            Punto p2 = buscarVertice(vertices, (i + 1) % n);
            prodCruz+=(p1.getX()*p2.getY())-(p1.getY()*p2.getX());
        }
        return prodCruz/2;
   }
   public double calcularPerimetro(List<Punto> vertices){
       double peri=0;
       int n=indicesVertices.length;
       for(int i=0;i<n;i++){
           Punto p1= buscarVertice(vertices,i);
           Punto p2= buscarVertice(vertices,(i+1)%n);
           peri+=calcularDistancia(p1,p2);
       }
       return peri;
   }
    private double calcularDistancia(Punto p1, Punto p2) {
        double dx = p2.getX() - p1.getX();
        double dy = p2.getY() - p1.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
   public int calcularOrientacion(List<Punto> vertices){
        double area = calcularAreaConSigno(vertices);
        if (area > EPSILON) {
            return 1;
        }
        if (area < -EPSILON) {
            return -1;
        }
        return 0;
   }

    public List<Poligono> triangular(List<Punto> vertices) {
        String error = validar(vertices);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }
        int orientacion = calcularOrientacion(vertices);
        List<Integer> restantes = new ArrayList<>();
        for (int indice : indicesVertices) {
            restantes.add(indice);
        }
        List<Poligono> triangulos = new ArrayList<>();
        while (restantes.size() > 3) {
            int n = restantes.size();
            int oreja = buscarOreja(vertices, restantes, orientacion);
            if (oreja != -1) {
                triangulos.add(new Poligono(restantes.get((oreja - 1 + n) % n), restantes.get(oreja), restantes.get((oreja + 1) % n)));
                restantes.remove(oreja);
                continue;
            }
            int colineal = buscarVerticeColineal(vertices, restantes);
            if (colineal == -1) {
                throw new IllegalStateException("No se encontró ninguna oreja.");
            }
            restantes.remove(colineal);
        }
        triangulos.add(new Poligono(restantes.get(0), restantes.get(1), restantes.get(2)));
        return triangulos;
    }

    public boolean seEnciman(Poligono otro, List<Punto> vertices) {
        List<Poligono> triangulosPropios = triangular(vertices);
        List<Poligono> triangulosOtro = otro.triangular(vertices);
        for (Poligono t1 : triangulosPropios) {
            for (Poligono t2 : triangulosOtro) {
                if (triangulosSeEnciman(t1.obtenerPuntos(vertices), t2.obtenerPuntos(vertices))) {
                    return true;
                }
            }
        }
        return false;
    }

    private Punto[] obtenerPuntos(List<Punto> vertices) {
        Punto[] puntos = new Punto[indicesVertices.length];
        for (int i = 0; i < indicesVertices.length; i++) {
            puntos[i] = buscarVertice(vertices, i);
        }
        return puntos;
    }

    private static boolean triangulosSeEnciman(Punto[] t1, Punto[] t2) {
        return !hayEjeSeparador(t1, t1, t2) && !hayEjeSeparador(t2, t1, t2);
    }

    private static boolean hayEjeSeparador(Punto[] triangulo, Punto[] t1, Punto[] t2) {
        for (int i = 0; i < 3; i++) {
            Punto a = triangulo[i];
            Punto b = triangulo[(i + 1) % 3];
            double ejeX = -(b.getY() - a.getY());
            double ejeY = b.getX() - a.getX();
            double longitud = Math.hypot(ejeX, ejeY);
            ejeX /= longitud;
            ejeY /= longitud;
            double min1 = Double.POSITIVE_INFINITY, max1 = Double.NEGATIVE_INFINITY;
            double min2 = Double.POSITIVE_INFINITY, max2 = Double.NEGATIVE_INFINITY;
            for (int j = 0; j < 3; j++) {
                double proyeccion1 = t1[j].getX() * ejeX + t1[j].getY() * ejeY;
                double proyeccion2 = t2[j].getX() * ejeX + t2[j].getY() * ejeY;
                min1 = Math.min(min1, proyeccion1);
                max1 = Math.max(max1, proyeccion1);
                min2 = Math.min(min2, proyeccion2);
                max2 = Math.max(max2, proyeccion2);
            }
            if (max1 <= min2 + EPSILON || max2 <= min1 + EPSILON) {
                return true;
            }
        }
        return false;
    }

    private static int buscarOreja(List<Punto> vertices, List<Integer> restantes, int orientacion) {
        int n = restantes.size();
        for (int i = 0; i < n; i++) {
            int anterior = (i - 1 + n) % n;
            int siguiente = (i + 1) % n;
            Punto a = vertices.get(restantes.get(anterior));
            Punto b = vertices.get(restantes.get(i));
            Punto c = vertices.get(restantes.get(siguiente));
            if (productoCruz(a, b, c) * orientacion <= EPSILON) {
                continue;
            }
            boolean esOreja = true;
            for (int j = 0; j < n && esOreja; j++) {
                if (j != anterior && j != i && j != siguiente && estaEnTriangulo(vertices.get(restantes.get(j)), a, b, c)) {
                    esOreja = false;
                }
            }
            if (esOreja) {
                return i;
            }
        }
        return -1;
    }

    private static int buscarVerticeColineal(List<Punto> vertices, List<Integer> restantes) {
        int n = restantes.size();
        for (int i = 0; i < n; i++) {
            Punto a = vertices.get(restantes.get((i - 1 + n) % n));
            Punto b = vertices.get(restantes.get(i));
            Punto c = vertices.get(restantes.get((i + 1) % n));
            if (Math.abs(productoCruz(a, b, c)) < EPSILON) {
                return i;
            }
        }
        return -1;
    }

    private static boolean estaEnTriangulo(Punto p, Punto a, Punto b, Punto c) {
        double d1 = productoCruz(a, b, p);
        double d2 = productoCruz(b, c, p);
        double d3 = productoCruz(c, a, p);
        boolean hayNegativo = d1 < -EPSILON || d2 < -EPSILON || d3 < -EPSILON;
        boolean hayPositivo = d1 > EPSILON || d2 > EPSILON || d3 > EPSILON;
        return !(hayNegativo && hayPositivo);
    }

    public String validar(List<Punto> vertices) {
        int n = indicesVertices.length;
        if (n < 3) {
            return "Un polígono necesita al menos 3 puntos.";
        }
        for (int i = 0; i < n; i++) {
            int idx = indicesVertices[i];
            if (idx < 0 || idx >= vertices.size()) {
                return "El punto P" + idx + " no existe.";
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (indicesVertices[i] == indicesVertices[j]) {
                    return "El punto P" + indicesVertices[i] + " está repetido.";
                }
                if (mismasCoordenadas(buscarVertice(vertices, i), buscarVertice(vertices, j))) {
                    return "Los puntos P" + indicesVertices[i] + " y P" + indicesVertices[j] + " tienen las mismas coordenadas.";
                }
            }
        }
        if (!esSimple(vertices)) {
            return "Las aristas del polígono se cruzan o se enciman.";
        }
        if (Math.abs(calcularAreaConSigno(vertices)) < EPSILON) {
            return "Los puntos son colineales (área cero).";
        }
        return null;
    }

    public boolean esSimple(List<Punto> vertices) {
        int n = indicesVertices.length;
        for (int i = 0; i < n; i++) {
            Punto a = buscarVertice(vertices, i);
            Punto b = buscarVertice(vertices, (i + 1) % n);
            Punto c = buscarVertice(vertices, (i + 2) % n);
            if (Math.abs(productoCruz(a, b, c)) < EPSILON && productoPunto(b, a, c) > 0) {
                return false;
            }
            for (int j = i + 2; j < n; j++) {
                if (i == 0 && j == n - 1) {
                    continue;
                }
                Punto d = buscarVertice(vertices, j);
                Punto e = buscarVertice(vertices, (j + 1) % n);
                if (segmentosSeTocan(a, b, d, e)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean segmentosSeTocan(Punto a, Punto b, Punto c, Punto d) {
        double d1 = productoCruz(a, b, c);
        double d2 = productoCruz(a, b, d);
        double d3 = productoCruz(c, d, a);
        double d4 = productoCruz(c, d, b);
        if (((d1 > EPSILON && d2 < -EPSILON) || (d1 < -EPSILON && d2 > EPSILON))
                && ((d3 > EPSILON && d4 < -EPSILON) || (d3 < -EPSILON && d4 > EPSILON))) {
            return true;
        }
        return (Math.abs(d1) < EPSILON && estaEnSegmento(a, b, c))
                || (Math.abs(d2) < EPSILON && estaEnSegmento(a, b, d))
                || (Math.abs(d3) < EPSILON && estaEnSegmento(c, d, a))
                || (Math.abs(d4) < EPSILON && estaEnSegmento(c, d, b));
    }

    private static double productoCruz(Punto origen, Punto p1, Punto p2) {
        return (p1.getX() - origen.getX()) * (p2.getY() - origen.getY())
                - (p1.getY() - origen.getY()) * (p2.getX() - origen.getX());
    }

    private static double productoPunto(Punto origen, Punto p1, Punto p2) {
        return (p1.getX() - origen.getX()) * (p2.getX() - origen.getX())
                + (p1.getY() - origen.getY()) * (p2.getY() - origen.getY());
    }

    private static boolean estaEnSegmento(Punto a, Punto b, Punto p) {
        return p.getX() >= Math.min(a.getX(), b.getX()) - EPSILON
                && p.getX() <= Math.max(a.getX(), b.getX()) + EPSILON
                && p.getY() >= Math.min(a.getY(), b.getY()) - EPSILON
                && p.getY() <= Math.max(a.getY(), b.getY()) + EPSILON;
    }

    private static boolean mismasCoordenadas(Punto p1, Punto p2) {
        return Math.abs(p1.getX() - p2.getX()) < EPSILON && Math.abs(p1.getY() - p2.getY()) < EPSILON;
    }

    public int[] getIndicesVertices() {
        return indicesVertices;
    }

    public void setIndicesVertices(int[] indicesVertices) {
        this.indicesVertices = indicesVertices;
    }
}
