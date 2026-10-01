package Modelos;

import java.awt.*;

public class Punto {

    private static int contador=0;
    private final int indice;
    private double x;
    private double y;


    Color color;

    public Punto(double x, double y){
        this.indice=contador++;
        this.x = x;
        this.y = y;

    }
    public void trasladar(double sumax, double sumay){

        this.x += sumax;
        this.y += sumay;

    }
    public void setPosicion(double x, double y){

        this.x = x;
        this.y = y;

    }

    public int getIndice(){
        return indice;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
