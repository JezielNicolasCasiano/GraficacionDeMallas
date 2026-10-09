package Modelos.PlanoCartesiano;

public class Punto3D extends Punto<Punto3D, Vector3D> {

    public Punto3D(double ordenadaX, double ordenadaY, double ordenadaZ) {
        this.ordenadaX = ordenadaX;
        this.ordenadaY = ordenadaY;
        this.ordenadaZ = ordenadaZ;
    }

    public double getOrdenadaX() {
        return ordenadaX;
    }

    public void setOrdenadaX(double ordenadaX) {
        this.ordenadaX = ordenadaX;
    }

    public double getOrdenadaY() {
        return ordenadaY;
    }

    public void setOrdenadaY(double ordenadaY) {
        this.ordenadaY = ordenadaY;
    }

    public double getOrdenadaZ() {
        return ordenadaZ;
    }

    public void setOrdenadaZ(double ordenadaZ) {
        this.ordenadaZ = ordenadaZ;
    }

    @Override
    public double obtenerDistanciaPunto(Punto3D p) {
        double dx = this.ordenadaX - p.ordenadaX;
        double dy = this.ordenadaY - p.ordenadaY;
        double dz = this.ordenadaZ - p.ordenadaZ;
        return (double) Math.sqrt(dx*dx + dy*dy + dz*dz);
    }

    @Override
    public Vector3D vectorHastaPunto(Punto3D p){
        return new Vector3D(p.ordenadaX - this.ordenadaX,p.ordenadaY - this.ordenadaY,p.ordenadaZ-this.ordenadaZ);
    }

    @Override
    public Punto3D trasladarPunto(Vector3D v){
        return new Punto3D(this.ordenadaX + v.getVectorX(), this.ordenadaY + v.getVectorY(),this.ordenadaZ+v.getVectorZ());
    }

}
