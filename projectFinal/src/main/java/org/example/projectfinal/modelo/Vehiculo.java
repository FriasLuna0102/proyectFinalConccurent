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
    private boolean detenido;
    private boolean detenidoUnaVez;
    private long tiempoDetenido;

    public Vehiculo(String id, TipoVehiculo tipo, Direccion direccion, EstadoVehiculo estado, double posX, double posY, double velocidad) {
        this.id = id;
        this.tipo = tipo;
        this.direccion = direccion;
        this.estado = estado;
        this.posX = posX;
        this.posY = posY;
        this.velocidad = velocidad;
        this.detenido = false;
        this.detenidoUnaVez = false;
        this.tiempoDetenido = 0;
    }

    public void dibujar(GraphicsContext gc) {
        gc.setFill(tipo == TipoVehiculo.NORMAL ? Color.BLUE : Color.RED); // Color azul para vehículos normales y rojo para emergencia
        gc.fillOval(posX, posY, 20, 10); // Ejemplo de dibujo de vehículo como óvalo
    }

    public void mover() {
        if (!detenido) {
            switch (direccion) {
                case DERECHA:
                    posX += velocidad * 5; // Aumento de velocidad
                    break;
                case IZQUIERDA:
                    posX -= velocidad * 5;
                    break;
                case RECTO:
                    posY -= velocidad * 5;
                    break;
                case VUELTA_EN_U:
                    posY += velocidad * 5;
                    break;
                default:
                    break;
            }
        }
    }

    public void detenerPorTresSegundos() {
        if (!detenido) {
            this.detenido = true;
            this.tiempoDetenido = System.currentTimeMillis();
        } else if (System.currentTimeMillis() - this.tiempoDetenido >= 3000) {
            this.detenido = false;
        }
    }

    public void detener() {
        this.detenido = true;
        this.tiempoDetenido = System.currentTimeMillis();
    }

    public void reanudar() {
        this.detenido = false;
        this.detenidoUnaVez = false;
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

    public TipoVehiculo getTipo() {
        return tipo;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public EstadoVehiculo getEstado() {
        return estado;
    }

    public boolean isDetenido() {
        return detenido;
    }

    public boolean isDetenidoUnaVez() {
        return detenidoUnaVez;
    }

    public long getTiempoDetenido() {
        return tiempoDetenido;
    }
}
