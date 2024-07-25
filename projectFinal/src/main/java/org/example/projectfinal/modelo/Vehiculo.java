package org.example.projectfinal.modelo;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.example.projectfinal.enumeraciones.*;

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
    private Accion accion;
    private boolean accionAplicada = false;
    private TipoCarril tipoCarril;
    private DoblarDonde doblarDonde;

    public Vehiculo(String id, TipoVehiculo tipo, Direccion direccion, EstadoVehiculo estado, double posX, double posY, double velocidad, Accion accion) {
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
        this.accion = accion;
    }

    public Vehiculo(String id, TipoVehiculo tipo, EstadoVehiculo estado, double posX, double posY, double velocidad, Accion accion, TipoCarril tipoCarril, DoblarDonde doblarDonde, Direccion direccion) {
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
        this.accion = accion;
        this.tipoCarril = tipoCarril;
        this.doblarDonde = doblarDonde;
    }

    public boolean getAccionAplicada() {
        return accionAplicada;
    }

    public void setAccionAplicada(boolean accionAplicada) {
        this.accionAplicada = accionAplicada;
    }
    public void dibujar(GraphicsContext gc) {
        gc.save(); // Guardar el estado actual del contexto gráfico

        switch (direccion) {
            case DERECHA:
                gc.setFill(tipo == TipoVehiculo.NORMAL ? Color.BLUE : Color.RED);
                gc.fillOval(posX, posY, 20, 10);
                break;
            case IZQUIERDA:
                gc.setFill(tipo == TipoVehiculo.NORMAL ? Color.BLUE : Color.RED);
                gc.fillOval(posX, posY, 20, 10);
                break;
            case ABAJO:
                gc.translate(posX, posY); // Trasladar el contexto gráfico al punto del vehículo
                gc.rotate(90); // Rotar 90 grados para que apunte hacia abajo
                gc.translate(-posX, -posY); // Volver a la traslación original
                gc.setFill(tipo == TipoVehiculo.NORMAL ? Color.BLUE : Color.RED);
                gc.fillOval(posX, posY, 20, 10);
                break;
            case ARRIBA:
                gc.translate(posX, posY); // Trasladar el contexto gráfico al punto del vehículo
                gc.rotate(-90); // Rotar -90 grados para que apunte hacia arriba
                gc.translate(-posX, -posY); // Volver a la traslación original
                gc.setFill(tipo == TipoVehiculo.NORMAL ? Color.BLUE : Color.RED);
                gc.fillOval(posX, posY, 20, 10);
                break;
        }

        gc.restore(); // Restaurar el estado original del contexto gráfico
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
                case ABAJO:
                    posY += velocidad * 5; // Movimiento hacia abajo
                    break;
                case ARRIBA:
                    posY -= velocidad * 5; // Movimiento hacia arriba
                    break;
                default:
                    break;
            }
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTipo(TipoVehiculo tipo) {
        this.tipo = tipo;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public void setEstado(EstadoVehiculo estado) {
        this.estado = estado;
    }

    public void setDetenido(boolean detenido) {
        this.detenido = detenido;
    }

    public void setDetenidoUnaVez(boolean detenidoUnaVez) {
        this.detenidoUnaVez = detenidoUnaVez;
    }

    public void setTiempoDetenido(long tiempoDetenido) {
        this.tiempoDetenido = tiempoDetenido;
    }

    public Accion getAccion() {
        return accion;
    }

    public void setAccion(Accion accion) {
        this.accion = accion;
    }

    public boolean isAccionAplicada() {
        return accionAplicada;
    }

    public TipoCarril getTipoCarril() {
        return tipoCarril;
    }

    public void setTipoCarril(TipoCarril tipoCarril) {
        this.tipoCarril = tipoCarril;
    }

    public DoblarDonde getDoblarDonde() {
        return doblarDonde;
    }

    public void setDoblarDonde(DoblarDonde doblarDonde) {
        this.doblarDonde = doblarDonde;
    }
}
