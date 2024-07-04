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
    private double x;
    private double y;
    private double velocidad;

    public Vehiculo(String id, TipoVehiculo tipo, Direccion direccion, EstadoVehiculo estado, double x, double y, double velocidad) {
        this.id = id;
        this.tipo = tipo;
        this.direccion = direccion;
        this.estado = estado;
        this.x = x;
        this.y = y;
        this.velocidad = velocidad;
    }

    public void dibujar(GraphicsContext gc) {
        if (tipo == TipoVehiculo.NORMAL) {
            gc.setFill(Color.BLUE);
        } else if (tipo == TipoVehiculo.EMERGENCIA) {
            gc.setFill(Color.RED);
        }
        gc.fillRect(x, y, 10, 10);
    }

    public void mover() {
        if (estado == EstadoVehiculo.EN_MOVIMIENTO) {
            switch (direccion) {
                case DERECHA:
                    x += velocidad;
                    break;
                case IZQUIERDA:
                    x -= velocidad;
                    break;
                case RECTO:
                    y -= velocidad;
                    break;
                case VUELTA_EN_U:
                    // Aquí podríamos hacer un giro en U, por simplicidad, volvemos a la posición original
                    y += velocidad;
                    break;
            }
        }
    }

    // Getters y setters
}
