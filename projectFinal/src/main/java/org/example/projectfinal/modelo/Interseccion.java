package org.example.projectfinal.modelo;

import org.example.projectfinal.enumeraciones.Direccion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interseccion {
    private String id;                        // Identificador único de la intersección
    private List<Vehiculo> vehiculos;         // Lista de vehículos en la intersección
    private Map<Direccion, Semaforo> semaforos; // Mapa de semáforos por dirección


    public Interseccion(String id, List<Vehiculo> vehiculos, Map<Direccion, Semaforo> semaforos) {
        this.id = id;
        this.vehiculos = vehiculos;
        this.semaforos = semaforos;
    }


    public Interseccion(String id) {
        this.id = id;
        this.vehiculos = new ArrayList<>();
        this.semaforos = new HashMap<>(); // Inicializar el mapa de semáforos
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(List<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }

    public Map<Direccion, Semaforo> getSemaforos() {
        return semaforos;
    }

    public void setSemaforos(Map<Direccion, Semaforo> semaforos) {
        this.semaforos = semaforos;
    }

    //Metodos:



}
