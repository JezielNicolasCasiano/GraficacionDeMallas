package Modelos.PlanoCartesiano;

public abstract class Punto <P extends Punto<P,V>, V extends Vector<V>>{
    protected double ordenadaX;
    protected double ordenadaY;
    protected double ordenadaZ;

    public abstract double obtenerDistanciaPunto(P p);
    public abstract V vectorHastaPunto(P p);
    public abstract P trasladarPunto(V v);
}
