package Modelos.PlanoCartesiano;

public class Punto2D extends Punto<Punto2D, Vector2D>{

    public Punto2D(double ordenadaX, double ordenadaY) {
        this.ordenadaX = ordenadaX;
        this.ordenadaY = ordenadaY;
    }

    public double getOrdenadaY() {
        return ordenadaY;
    }

    public void setOrdenadaY(double ordenadaY) {
        this.ordenadaY = ordenadaY;
    }

    public double getOrdenadaX() {return ordenadaX;}

    public void setOrdenadaX(double ordenadaX) {
        this.ordenadaX = ordenadaX;
    }

    @Override
    public double obtenerDistanciaPunto(Punto2D p) {
        double dx = this.ordenadaX - p.ordenadaX;
        double dy = this.ordenadaY - p.ordenadaY;
        return (double) Math.sqrt(dx*dx + dy*dy);
    }

    @Override
    public Vector2D vectorHastaPunto(Punto2D p){
        return new Vector2D(p.ordenadaX - this.ordenadaX,p.ordenadaY - this.ordenadaY);
    }

    @Override
    public Punto2D trasladarPunto(Vector2D v){
        return new Punto2D(this.ordenadaX + v.getVectorX(), this.ordenadaY + v.getVectorY());
    }
    @Override
    public String toString() {
        return String.format("P(%.2f, %.2f)", ordenadaX, ordenadaY);
    }
}
