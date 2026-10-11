package Modelos;

public class FilaTriangulo {
    private final int orden;
    private final int idPoligono;
    private final String vertices;

    public FilaTriangulo(int orden, int idPoligono, String vertices) {
        this.orden = orden;
        this.idPoligono = idPoligono;
        this.vertices = vertices;
    }

    public int getOrden() { return orden; }
    public int getIdPoligono() { return idPoligono; }
    public String getVertices() { return vertices; }
}
