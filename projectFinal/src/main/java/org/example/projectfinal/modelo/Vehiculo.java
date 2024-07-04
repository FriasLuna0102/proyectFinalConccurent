package org.example.projectfinal.modelo;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.example.projectfinal.enumeraciones.Direccion;
import org.example.projectfinal.enumeraciones.EstadoVehiculo;
import org.example.projectfinal.enumeraciones.TipoVehiculo;

public class Vehiculo {
    private String id;
    private TipoVehiculo tipo;
    private Direccion direccion;
    private EstadoVehiculo estado;
    private double posX;
    private double posY;
    private double velocidad;

    public Vehiculo(String id, TipoVehiculo tipo, Direccion direccion, EstadoVehiculo estado, double posX, double posY, double velocidad) {
        this.id = id;
        this.tipo = tipo;
        this.direccion = direccion;
        this.estado = estado;
        this.posX = posX;
        this.posY = posY;
        this.velocidad = velocidad;
    }

    public void dibujar(GraphicsContext gc) {
        gc.setFill(tipo == TipoVehiculo.NORMAL ? Color.BLUE : Color.RED); // Color azul para vehículos normales y rojo para emergencia
        gc.fillOval(posX, posY, 20, 10); // Ejemplo de dibujo de vehículo como óvalo
    }

    public void mover() {
        switch (direccion) {
            case DERECHA:
                posX += velocidad * 5; // Aumento de velocidad ()
                break;
            case IZQUIERDA:
                posX -= velocidad * 5;
                break;
            case RECTO:
                posX += velocidad * 5;
                break;
            case VUELTA_EN_U:
                posX -= velocidad * 5;
                break;
            default:
                break;
        }
    }

    // Getters y setters según sea necesario
    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(double velocidad) {
        this.velocidad = velocidad;
    }
}
