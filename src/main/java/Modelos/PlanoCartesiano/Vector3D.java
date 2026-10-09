package Modelos.PlanoCartesiano;

public class Vector3D extends Vector<Vector3D>{

    public Vector3D(double vectorX, double vectorY, double vectorZ) {
        this.vectorX = vectorX;
        this.vectorY = vectorY;
        this.vectorZ = vectorZ;
    }

    public double getVectorX() {
        return vectorX;
    }

    public void setVectorX(double vectorX) {
        this.vectorX = vectorX;
    }

    public double getVectorY() {
        return vectorY;
    }

    public void setVectorY(double vectorY) {
        this.vectorY = vectorY;
    }

    public double getVectorZ() {
        return vectorZ;
    }

    public void setVectorZ(double vectorZ) {
        this.vectorZ = vectorZ;
    }

    @Override
    public double obtenerMagnitud(){
        return (double) Math.sqrt(vectorX*vectorX+vectorY*vectorY+vectorZ*vectorZ);
    }

    @Override
    public Vector3D sumar(Vector3D v){
        return new Vector3D(this.vectorX+v.vectorX,this.vectorY+v.vectorY,this.vectorZ+v.vectorZ);
    }

    @Override
    public Vector3D restar(Vector3D v){
        return new Vector3D(this.vectorX-v.vectorX,this.vectorY-v.vectorY,this.vectorZ-v.vectorZ);
    }

    @Override
    public Vector3D multEscalar(double k){
        return new Vector3D(this.vectorX*k,this.vectorY*k,this.vectorZ*k);
    }

    @Override
    public double obtenerProductoPunto(Vector3D v){
        return (this.vectorX*v.vectorX)+(this.vectorY*v.vectorY)+(this.vectorZ*v.vectorZ);
    }

    @Override
    public Vector3D normalizar(){
        double mag=obtenerMagnitud();
        if (mag==0){
            throw new ArithmeticException("No se puede normalizar vector nulo");
        }
        return new Vector3D(vectorX/mag,vectorY/mag,vectorZ/mag);
    }
}
