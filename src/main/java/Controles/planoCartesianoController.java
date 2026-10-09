package Controles;

import Modelos.PlanoCartesiano.Plano;
import Modelos.PlanoCartesiano.Punto2D;
import Modelos.PlanoCartesiano.Vector2D;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;


import java.util.Locale;

public class planoCartesianoController {
    //Objetos de javaFX implementadas en la vista e inyectadas aca.
    @FXML
    private AnchorPane contenedorPrincipal;
    @FXML
    private Canvas lienzo;

    private GraphicsContext g; //Clase ya implementada para modelar un sistema de coordenadas en 2D
    private Plano planoMatematico; //Ya parte del modelo, seria la representación abstracta del plano carteiano
    private double escala = 50; //pixeles por cada unidad matematica
    private final double separacionDeseadaPixeles = 50; //separacion aproximada en pixeles entre lineas de la cuadricula
    private static final double escalaMin = 1, escalaMax = 1e6;

    //Tamaños en pixeles para que no cambien con el zoom
    private static final double radioPuntoPixeles = 4;
    private static final double longitudCabezaPixeles = 10; //se usa en la flecha y como margen de vectorFueraDeRango

    //variables para el funcionamiento del arrastre del mouse
    private double ultimoMouseX = 0;
    private double ultimoMouseY = 0;

    //Desplazamiento en pixeles del origen matematico respecto al centro del lienzo (un vector, no una posicion)
    private double desplazamientoOrigenX = 0;
    private double desplazamientoOrigenY = 0;


    //Variables relacionadas con el canvas y el graphscene
    private double anchoLienzo;
    private double altoLienzo;

    @FXML
    private void initialize() {
        g = lienzo.getGraphicsContext2D(); //inicializacion de la clase dedicada
        planoMatematico = new Plano(); //Instanciacion del plano del modelo

        lienzo.widthProperty().bind(contenedorPrincipal.widthProperty());
        lienzo.heightProperty().bind(contenedorPrincipal.heightProperty());

        lienzo.widthProperty().addListener((o, a, b) -> dibujar()); //Como lienzo es un canvas es necesario añadir listener para escuchar el cambio de posicion cuando se hace zoom out o in
        lienzo.heightProperty().addListener((o, a, b) -> dibujar());

        configurarZoom();
        configurarArrastre();
        dibujar();
    }


    //Método principipal para dibujar en el plano.
    private void dibujar() {
        altoLienzo = lienzo.getHeight();
        anchoLienzo = lienzo.getWidth();
        if (altoLienzo == 0 || anchoLienzo == 0) {
            return;
        }
        g.clearRect(0, 0, anchoLienzo, altoLienzo);
        dibujarCuadricula(calcularPaso());
        dibujarEjes();
        dibujarNumeros();
        for (Vector2D vectores : planoMatematico.getVectores2D()){
            dibujarVector2D(vectores.getVectorX(), vectores.getVectorY());
        }
        for (Punto2D punto2D : planoMatematico.getPuntos2D()){
            dibujarPunto2D(punto2D.getOrdenadaX(), punto2D.getOrdenadaY());
        }


    }

    public void dibujarEjes() {
        double pixelXDelEjeY = nitido(xMatematicoAPixel(0));
        double pixelYDelEjeX = nitido(yMatematicoAPixel(0));
        g.setLineWidth(1);
        g.setStroke(Color.BLACK);
        g.strokeLine(pixelXDelEjeY, 0, pixelXDelEjeY, altoLienzo);  // Eje y
        g.strokeLine(0, pixelYDelEjeX, anchoLienzo, pixelYDelEjeX);  //Eje x
    }

    public void dibujarCuadricula(double paso) {
        g.setLineWidth(0.5);
        g.setStroke(Color.rgb(126, 126, 126)); //color gris para cuadricula
        double xMinVisible = xPixelAMatematico(0); //Se calcula el minimoX visible
        double xMaxVisible = xPixelAMatematico(anchoLienzo); //Se calcula el maximoX visible
        double yMinVisible = yPixelAMatematico(altoLienzo); //Se calcula el minimoY visible
        double yMaxVisible = yPixelAMatematico(0); // Se calcula el maximoY visible
        long primerIndiceX = (long) Math.ceil(xMinVisible / paso);
        long ultimoIndiceX = (long) Math.floor(xMaxVisible / paso);
        long primerIndiceY = (long) Math.ceil(yMinVisible / paso);
        long ultimoIndiceY = (long) Math.floor(yMaxVisible / paso);
        for (long i = primerIndiceX; i <= ultimoIndiceX; i++) {
            double valorX = i * paso;
            double pixelX = nitido(xMatematicoAPixel(valorX));
            g.strokeLine(pixelX, 0, pixelX, altoLienzo);
        }
        for (long i = primerIndiceY; i <= ultimoIndiceY; i++) {
            double valorY = i * paso;
            double pixelY = nitido(yMatematicoAPixel(valorY));
            g.strokeLine(0, pixelY, anchoLienzo, pixelY);
        }
    }

    public double calcularPaso() {
        double pasoBruto = separacionDeseadaPixeles / escala;
        double exponente = Math.floor(Math.log10(pasoBruto));
        double base = Math.pow(10, exponente);
        double fraccion = pasoBruto / base;
        double factor;
        if (fraccion <= 1) factor = 1;
        else if (fraccion <= 2) factor = 2;
        else if (fraccion <= 5) factor = 5;
        else factor = 10;
        return factor * base;
    }


    private void configurarZoom() {
        lienzo.setOnScroll(e -> {
            if (e.getDeltaY() == 0) return;

            double factorZoom = Math.pow(1.1, e.getDeltaY() / 40.0);

            double nuevaEscala = Math.clamp(escala * factorZoom, escalaMin, escalaMax);
            factorZoom = nuevaEscala / escala; // factor real tras el límite

            double mouseX = e.getX(), mouseY = e.getY();
            desplazamientoOrigenX = (mouseX - (mouseX - pixelOrigenX()) * factorZoom) - anchoLienzo / 2;
            desplazamientoOrigenY = (mouseY - (mouseY - pixelOrigenY()) * factorZoom) - altoLienzo / 2;
            escala = nuevaEscala;

            dibujar();
            e.consume();
        });
    }

    private void configurarArrastre() {
        lienzo.setOnMousePressed(e -> {
            ultimoMouseX = e.getX();
            ultimoMouseY = e.getY();
        });

        lienzo.setOnMouseDragged(e -> {
            desplazamientoOrigenX += (e.getX() - ultimoMouseX);
            desplazamientoOrigenY += (e.getY() - ultimoMouseY);
            ultimoMouseX = e.getX();
            ultimoMouseY = e.getY();
            dibujar();
        });
    }

    private void dibujarNumeros() {
        //Se guarda la posicion sin limitar para despues saber si el clamp la movio (es decir, si quedo pegada a un borde)
        double pixelYFilaNumerosXSinLimitar = yMatematicoAPixel(0) + 3;
        double pixelYFilaNumerosX = Math.clamp(pixelYFilaNumerosXSinLimitar, 3, altoLienzo - 3 - g.getFont().getSize());
        double pixelXColumnaNumerosYSinLimitar = xMatematicoAPixel(0) - 3;
        double pixelXColumnaNumerosY = Math.clamp(pixelXColumnaNumerosYSinLimitar, 3, anchoLienzo - 3);

        boolean filaXPegada = pixelYFilaNumerosX != pixelYFilaNumerosXSinLimitar; //el eje X salio por arriba o por abajo
        boolean columnaYPegada = pixelXColumnaNumerosY != pixelXColumnaNumerosYSinLimitar; //el eje Y salio por la izquierda o por la derecha
        boolean columnaYPegadaIzquierda = columnaYPegada && pixelXColumnaNumerosY == 3;
        boolean origenVisible = !filaXPegada && !columnaYPegada; //solo entonces se encimarian los dos ceros
        double paso = calcularPaso();

        double xMinVisible = xPixelAMatematico(0); //Se calcula el minimoX visible
        double xMaxVisible = xPixelAMatematico(anchoLienzo); //Se calcula el maximoX visible
        double yMinVisible = yPixelAMatematico(altoLienzo); //Se calcula el minimoY visible
        double yMaxVisible = yPixelAMatematico(0); // Se calcula el maximoY visible
        long primerIndiceX = (long) Math.ceil(xMinVisible / paso);
        long ultimoIndiceX = (long) Math.floor(xMaxVisible / paso);
        long primerIndiceY = (long) Math.ceil(yMinVisible / paso);
        long ultimoIndiceY = (long) Math.floor(yMaxVisible / paso);
        int decimales = Math.max(0, (int) -Math.floor(Math.log10(paso)));
        g.setTextAlign(TextAlignment.CENTER);
        g.setTextBaseline(VPos.TOP);
        for (long i = primerIndiceX; i <= ultimoIndiceX; i++) {
            if (origenVisible && i == 0) continue; //el cero se dibuja una sola vez al final
            double valorX = i * paso;
            g.fillText(String.format(Locale.US, "%." + decimales + "f", valorX), xMatematicoAPixel(valorX), pixelYFilaNumerosX);
        }
        if (columnaYPegadaIzquierda) {
            g.setTextAlign(TextAlignment.LEFT);
        } else {
            g.setTextAlign(TextAlignment.RIGHT);
        }
        g.setTextBaseline(VPos.CENTER);
        for (long i = primerIndiceY; i <= ultimoIndiceY; i++) {
            if (origenVisible && i == 0) continue; //el cero se dibuja una sola vez al final
            double valorY = i * paso;
            g.fillText(String.format(Locale.US, "%." + decimales + "f", valorY), pixelXColumnaNumerosY, yMatematicoAPixel(valorY));
        }

        //Un solo "0" en la esquina inferior izquierda del cruce de los ejes, sin tocar ninguno de los dos
        if (origenVisible) {
            g.setTextAlign(TextAlignment.RIGHT);
            g.setTextBaseline(VPos.TOP);
            g.fillText("0", pixelXColumnaNumerosY, pixelYFilaNumerosX);
        }

    }
    private void dibujarVector2D(double ordenadaX, double ordenadaY){
        double ordenadaXPixel = xMatematicoAPixel(ordenadaX);
        double ordenadaYPixel = yMatematicoAPixel(ordenadaY);
        //Margen = largo de la cabeza, porque las aletas pueden asomarse aunque la punta quede justo afuera
        if (vectorFueraDeRango(pixelOrigenX(),pixelOrigenY(), ordenadaXPixel, ordenadaYPixel, longitudCabezaPixeles)){return;}
        g.setStroke(Color.BLACK); //¿Tal vez poner para que vaya cambiando de color conforme los puntos que se agreguen?
        g.setLineWidth(1);
        g.strokeLine(pixelOrigenX(), pixelOrigenY(), ordenadaXPixel, ordenadaYPixel);
        dibujarCabezaFlecha(ordenadaX, ordenadaY);
    }

    //La cabeza se calcula directamente en pixeles para que mida lo mismo con cualquier zoom
    private void dibujarCabezaFlecha(double ordenadaX, double ordenadaY) {
        if (ordenadaX == 0 && ordenadaY == 0) return;
        double xPuntaPixel = xMatematicoAPixel(ordenadaX);
        double yPuntaPixel = yMatematicoAPixel(ordenadaY);
        double dxPixel = xPuntaPixel - pixelOrigenX();
        double dyPixel = yPuntaPixel - pixelOrigenY();
        double anguloVector = Math.atan2(dyPixel, dxPixel);

        //Longitud fija en pixeles, pero nunca mas de un tercio del vector para que un vector muy corto no quede tapado por su cabeza
        double longitudCabeza = Math.min(longitudCabezaPixeles, Math.hypot(dxPixel, dyPixel) / 3);
        double anguloApertura = Math.toRadians(45);
        double anguloAleta1 = anguloVector + Math.PI - anguloApertura; // 180° - apertura
        double anguloAleta2 = anguloVector + Math.PI + anguloApertura; // 180° + apertura

        double xAleta1Pixel = xPuntaPixel + longitudCabeza * Math.cos(anguloAleta1);
        double yAleta1Pixel = yPuntaPixel + longitudCabeza * Math.sin(anguloAleta1);

        double xAleta2Pixel = xPuntaPixel + longitudCabeza * Math.cos(anguloAleta2);
        double yAleta2Pixel = yPuntaPixel + longitudCabeza * Math.sin(anguloAleta2);

        g.strokeLine(xPuntaPixel, yPuntaPixel, xAleta1Pixel, yAleta1Pixel);
        g.strokeLine(xPuntaPixel, yPuntaPixel, xAleta2Pixel, yAleta2Pixel);
    }

    private void dibujarPunto2D(double ordenadaX, double ordenadaY){
        double ordenadaXPixel = xMatematicoAPixel(ordenadaX);
        double ordenadaYPixel = yMatematicoAPixel(ordenadaY);
        if (pixelFueraDeRango(ordenadaXPixel, ordenadaYPixel, radioPuntoPixeles)) return;
        g.setFill(Color.BLACK);
        g.fillOval(ordenadaXPixel - radioPuntoPixeles, ordenadaYPixel - radioPuntoPixeles,
                2 * radioPuntoPixeles, 2 * radioPuntoPixeles);
    }

    private boolean pixelFueraDeRango(double pixelX, double pixelY, double margen){
        return pixelX < 0 - margen || pixelX > anchoLienzo + margen || pixelY < 0 - margen || pixelY > altoLienzo + margen;
    }

    private boolean vectorFueraDeRango(double x1, double y1, double x2, double y2, double margen){
        return x1 < 0 - margen && x2 < 0 - margen || x1 > anchoLienzo + margen && x2 > anchoLienzo + margen || y1 < 0 - margen && y2 < 0 - margen || y1 > altoLienzo + margen && y2 > altoLienzo + margen;
    }

    //Más metodos auxiliares: posicion en pixeles del origen = centro del lienzo + desplazamiento
    private double pixelOrigenX() {
        return desplazamientoOrigenX + anchoLienzo / 2;
    }

    private double pixelOrigenY() {
        return desplazamientoOrigenY + altoLienzo / 2;
    }


    /*Metodos para agregar puntos y vectores al plano.
    Primero se calcula si cabe y con que escala; solo si cabe se guarda y se dibuja.
    Asi, cuando no se puede representar, la escala nunca llego a modificarse y no hay que restaurarla.*/
    public boolean agregarPunto2D(double coordenadaX, double coordenadaY) {
        planoMatematico.crearPunto2D(coordenadaX, coordenadaY);
        dibujar();
        return true;
    }

    public boolean agregarVector2D(double coordenadaX, double coordenadaY) {
        planoMatematico.crearVector2D(coordenadaX, coordenadaY);
        dibujar();
        return true;
    }

    //Metodos auxiliares para transformar coordenadas cartesianas a Pixeles
    private double xMatematicoAPixel(double xMatematico) {
        return pixelOrigenX() + (xMatematico * escala);
    }

    private double yMatematicoAPixel(double yMatematico) {
        return pixelOrigenY() - (yMatematico * escala);
    }


    //metodo para evitar aliasing
    private double nitido(double pixel) {
        return Math.floor(pixel) + 0.5;
    }


    //Metodos auxiliares para transformar coordenadas Pixeles a cartesianas
    private double xPixelAMatematico(double pixelX) {
        return (pixelX - pixelOrigenX()) / escala;
    }

    private double yPixelAMatematico(double pixelY) {
        return (pixelOrigenY() - pixelY) / escala;
    }

    public Canvas getLienzo() {
        return lienzo;
    }
}
