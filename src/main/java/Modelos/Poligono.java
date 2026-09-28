package Modelos;

public class Poligono {

    private int[] indicesVertices;

    public Poligono(int... indicesVertices){

        this.indicesVertices = indicesVertices;

    }

    public void mostrar(){



    }
   public double calcularArea(){

  return 0.0;

   }
   public double calcularPerimetro(){

        return 0.0;

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
