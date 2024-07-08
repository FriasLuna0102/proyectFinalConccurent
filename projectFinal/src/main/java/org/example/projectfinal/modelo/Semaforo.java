package org.example.projectfinal.modelo;

import org.example.projectfinal.enumeraciones.EstadoSemaforo;

public class Semaforo {
    private String id;
    private EstadoSemaforo estado;
    private int tiempoVerde;
    private int tiempoRojo;
    private int tiempoAmarillo;
    private long tiempoInicio;

    public Semaforo(String id, EstadoSemaforo estado, int tiempoVerde, int tiempoRojo, int tiempoAmarillo) {
        this.id = id;
        this.estado = estado;
        this.tiempoVerde = tiempoVerde;
        this.tiempoRojo = tiempoRojo;
        this.tiempoAmarillo = tiempoAmarillo;
        this.tiempoInicio = System.currentTimeMillis();
    }

    public EstadoSemaforo getEstado() {
        return estado;
    }

    public void cambiarEstado(EstadoSemaforo nuevoEstado) {
        this.estado = nuevoEstado;
        this.tiempoInicio = System.currentTimeMillis();
    }

    public void actualizarEstado() {
        long tiempoActual = System.currentTimeMillis();
        long tiempoTranscurrido = (tiempoActual - tiempoInicio) / 2000;

        switch (estado) {
            case ROJO:
                if (tiempoTranscurrido >= tiempoRojo) {
                    cambiarEstado(EstadoSemaforo.VERDE);
                }
                break;
            case VERDE:
                if (tiempoTranscurrido >= tiempoVerde) {
                    cambiarEstado(EstadoSemaforo.AMARILLO);
                }
                break;
            case AMARILLO:
                if (tiempoTranscurrido >= tiempoAmarillo) {
                    cambiarEstado(EstadoSemaforo.ROJO);
                }
                break;
        }
    }
}
