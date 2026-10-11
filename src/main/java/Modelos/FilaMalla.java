package Modelos;

public class FilaMalla {
    private final int idPoligono;
    private final String puntos;

    public FilaMalla(int idPoligono, String puntos) {
        this.idPoligono = idPoligono;
        this.puntos = puntos;
    }

    public int getIdPoligono() { return idPoligono; }
    public String getPuntos() { return puntos; }
}
