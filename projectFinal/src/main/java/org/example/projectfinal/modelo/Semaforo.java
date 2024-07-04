package org.example.projectfinal.modelo;

import org.example.projectfinal.enumeraciones.EstadoSemaforo;

public class Semaforo {
    private String id;
    private EstadoSemaforo estado;
    private int tiempoVerde;
    private int tiempoRojo;
    private int tiempoAmarillo;

    public Semaforo(String id, EstadoSemaforo estado, int tiempoVerde, int tiempoRojo, int tiempoAmarillo) {
        this.id = id;
        this.estado = estado;
        this.tiempoVerde = tiempoVerde;
        this.tiempoRojo = tiempoRojo;
        this.tiempoAmarillo = tiempoAmarillo;
    }

    public EstadoSemaforo getEstado() {
        return estado;
    }

    public void cambiarEstado(EstadoSemaforo nuevoEstado) {
        this.estado = nuevoEstado;
    }

    // Métodos para manejar los tiempos y cambios de estado
}
