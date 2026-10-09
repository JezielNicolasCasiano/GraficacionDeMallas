package Modelos.PlanoCartesiano;

public abstract class Vector<V extends Vector<V>> {
    protected double vectorX;
    protected double vectorY;
    protected double vectorZ;

    public abstract V sumar(V v);
    public abstract V restar(V v);
    public abstract V multEscalar(double k);
    public abstract double obtenerProductoPunto(V v);
    public abstract double obtenerMagnitud();
    public abstract V normalizar();

}
