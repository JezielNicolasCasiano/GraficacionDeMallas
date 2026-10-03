package Modelos;

import java.util.List;

public class Poligono {

    private int[] indicesVertices;

    public Poligono(int... indicesVertices){

        this.indicesVertices = indicesVertices;

    }

    public Punto buscarVertice(List<Punto> vertices,int i){
        int indiceGlobal = this.indicesVertices[i];
        return vertices.get(indiceGlobal);
    }

    public void mostrar(){



    }
   public double calcularArea(List<Punto> vertices){
        double prodCruz=0;
        int n=indicesVertices.length;
        for(int i=0;i<n;i++){

            Punto p1 = buscarVertice(vertices, i);
            Punto p2 = buscarVertice(vertices, (i + 1) % n);
            prodCruz+=(p1.getX()*p2.getY())-(p1.getY()*p2.getX());
        }
        return Math.abs(prodCruz/2);
   }
   public double calcularPerimetro(List<Punto> vertices){
       double peri=0;
       int n=indicesVertices.length;
       for(int i=0;i<n;i++){
           Punto p1= buscarVertice(vertices,indicesVertices[i]);
           Punto p2= buscarVertice(vertices,indicesVertices[(i+1)%n]);
           peri+=calcularDistancia(p1,p2);
       }
       return peri;
   }
    private double calcularDistancia(Punto p1, Punto p2) {
        double dx = p2.getX() - p1.getX();
        double dy = p2.getY() - p1.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
   public int calcularOrientacion(){


        return 0;
   }

    public int[] getIndicesVertices() {
        return indicesVertices;
    }

    public void setIndicesVertices(int[] indicesVertices) {
        this.indicesVertices = indicesVertices;
    }
}
