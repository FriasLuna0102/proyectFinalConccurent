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



}
