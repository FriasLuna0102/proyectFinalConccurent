package org.example.projectfinal.modelo;

import org.example.projectfinal.enumeraciones.EstadoSemaforo;

public class Semaforo {
    private String id;               // Identificador único del semáforo
    private EstadoSemaforo estado;   // Estado del semáforo (rojo, amarillo, verde)
    private int tiempoVerde;         // Tiempo que el semáforo permanece en verde
    private int tiempoRojo;          // Tiempo que el semáforo permanece en rojo
    private int tiempoAmarillo;      // Tiempo que el semáforo permanece en amarillo


    public Semaforo(String id, EstadoSemaforo estado, int tiempoVerde, int tiempoRojo, int tiempoAmarillo) {
        this.id = id;
        this.estado = estado;
        this.tiempoVerde = tiempoVerde;
        this.tiempoRojo = tiempoRojo;
        this.tiempoAmarillo = tiempoAmarillo;
    }

    public Semaforo(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EstadoSemaforo getEstado() {
        return estado;
    }

    public void setEstado(EstadoSemaforo estado) {
        this.estado = estado;
    }

    public int getTiempoVerde() {
        return tiempoVerde;
    }

    public void setTiempoVerde(int tiempoVerde) {
        this.tiempoVerde = tiempoVerde;
    }

    public int getTiempoRojo() {
        return tiempoRojo;
    }

    public void setTiempoRojo(int tiempoRojo) {
        this.tiempoRojo = tiempoRojo;
    }

    public int getTiempoAmarillo() {
        return tiempoAmarillo;
    }

    public void setTiempoAmarillo(int tiempoAmarillo) {
        this.tiempoAmarillo = tiempoAmarillo;
    }


    //Metodos:

    public boolean puedeAvanzar() {
        return this.estado == EstadoSemaforo.VERDE;
    }

    public void cambiarEstado() {
        // Lógica para cambiar el estado del semáforo
        switch (this.estado) {
            case ROJO:
                this.estado = EstadoSemaforo.VERDE;
                break;
            case VERDE:
                this.estado = EstadoSemaforo.AMARILLO;
                break;
            case AMARILLO:
                this.estado = EstadoSemaforo.ROJO;
                break;
        }
        System.out.println("Semaforo " + id + " cambiando a " + estado);
    }

    public void iniciarCiclo() {
        // Ciclo de cambio de luces
        while (true) {
            cambiarEstado();
            try {
                Thread.sleep(getTiempoEstadoActual());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private int getTiempoEstadoActual() {
        switch (this.estado) {
            case VERDE:
                return tiempoVerde;
            case ROJO:
                return tiempoRojo;
            case AMARILLO:
                return tiempoAmarillo;
            default:
                return 0;
        }
    }

}
