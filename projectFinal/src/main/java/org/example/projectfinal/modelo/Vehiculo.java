package org.example.projectfinal.modelo;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
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
    private final Image iconoVehiculoNormal;
    private final Image iconoVehiculoEmergencia;

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
        iconoVehiculoNormal = new Image(getClass().getResource("/image/car.png").toString());
        iconoVehiculoEmergencia = new Image(getClass().getResource("/image/medical.png").toString());
    }

    public void dibujar(GraphicsContext gc) {
        Image imagenVehiculo = tipo == TipoVehiculo.NORMAL ? iconoVehiculoNormal : iconoVehiculoEmergencia;
        gc.drawImage(imagenVehiculo, posX, posY, 20, 10); // Dibujar imagen del vehículo
    }

    public void mover() {
        if (!detenido) {
            switch (direccion) {
                case DERECHA:
                    posX += velocidad * 20; // Aumento de velocidad
                    break;
                case IZQUIERDA:
                    posX -= velocidad * 20;
                    break;
                case RECTO:
                    posY -= velocidad * 20;
                    break;
                case VUELTA_EN_U:
                    posY += velocidad * 20;
                    break;
                case ARRIBA: // Movimiento hacia ARRIBA
                    posY -= velocidad * 20;
                    break;
                case ABAJO: // Movimiento hacia ABAJO
                    posY += velocidad * 20;
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
