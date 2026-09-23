package Modelos;

import java.awt.*;

public class Punto {

    private double x;
    private double y;
    private double z;

    Color color;

    public Punto(double x, double y, double z){

        this.x = x;
        this.y = y;
        this.z = z;
    }
    public void trasladar(double sumax, double sumay, double sumaz){

        this.x += sumax;
        this.y += sumay;
        this.z += sumaz;

    }
    public void setPosicion(double x, double y, double z){

        this.x = x;
        this.y = y;
        this.z = z;

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

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
