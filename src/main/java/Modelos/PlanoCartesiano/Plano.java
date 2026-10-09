package Modelos.PlanoCartesiano;
import java.util.ArrayList;

public class Plano {

    ArrayList<Punto2D> puntos2D = new ArrayList<>();
    ArrayList<Vector2D> vectores2D = new ArrayList<>();
    ArrayList<Vector3D> vectores3D = new ArrayList<>();
    ArrayList<Punto3D> puntos3D = new ArrayList<>();

    public void crearVector3D(double ordenadaX, double ordenadaY, double ordenadaZ){
        vectores3D.add(new Vector3D(ordenadaX,ordenadaY, ordenadaZ));
    }

    public void crearVector2D(double ordenadaX, double ordenadaY){
        vectores2D.add(new Vector2D(ordenadaX,ordenadaY));
    }

    public void crearPunto3D(double ordenadaX, double ordenadaY, double ordenadaZ){
        puntos3D.add(new Punto3D(ordenadaX, ordenadaY, ordenadaZ));
    }

    public void crearPunto2D(double ordenadaX, double ordenadaY){
        puntos2D.add(new Punto2D(ordenadaX, ordenadaY));
    }

    //getters y setters


    public ArrayList<Punto2D> getPuntos2D() {
        return puntos2D;
    }

    public void setPuntos2D(ArrayList<Punto2D> puntos2D) {
        this.puntos2D = puntos2D;
    }

    public ArrayList<Vector2D> getVectores2D() {
        return vectores2D;
    }

    public void setVectores2D(ArrayList<Vector2D> vectores2D) {
        this.vectores2D = vectores2D;
    }

    public ArrayList<Vector3D> getVectores3D() {
        return vectores3D;
    }

    public void setVectores3D(ArrayList<Vector3D> vectores3D) {
        this.vectores3D = vectores3D;
    }

    public ArrayList<Punto3D> getPuntos3D() {
        return puntos3D;
    }

    public void setPuntos3D(ArrayList<Punto3D> puntos3D) {
        this.puntos3D = puntos3D;
    }
}
