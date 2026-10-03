package Modelos;

public class FilaMalla {
    private final int idPoligono;
    private final String puntos;
    private final double area;
    private final double perimetro;

    public FilaMalla(int idPoligono, String puntos, double area, double perimetro) {
        this.idPoligono = idPoligono;
        this.puntos = puntos;
        this.area = area;
        this.perimetro = perimetro;
    }

    public int getIdPoligono() { return idPoligono; }
    public String getPuntos() { return puntos; }
    public double getArea() { return area; }
    public double getPerimetro() { return perimetro; }
}
