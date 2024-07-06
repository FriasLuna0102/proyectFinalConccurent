package org.example.projectfinal.modelo;

import org.example.projectfinal.enumeraciones.Direccion;
import org.example.projectfinal.enumeraciones.EstadoSemaforo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Interseccion {
    private String id;
    private Map<Direccion, ConcurrentLinkedQueue<Vehiculo>> vehiculosPorDireccion;
    private Map<Direccion, Semaforo> semaforos;

    public Interseccion(String id) {
        this.id = id;
        this.vehiculosPorDireccion = new HashMap<>();
        this.semaforos = new HashMap<>();

        // Inicializar semáforos
        for (Direccion direccion : Direccion.values()) {
            semaforos.put(direccion, new Semaforo(direccion.name(), EstadoSemaforo.ROJO, 10, 5, 2)); // Valores de ejemplo para tiempo de cada estado
        }
    }

    public void agregarVehiculo(Direccion direccion, Vehiculo vehiculo) {
        vehiculosPorDireccion.computeIfAbsent(direccion, k -> new ConcurrentLinkedQueue<>()).add(vehiculo);
    }

    public void controlarSemaforos() {
        // Lógica para cambiar los semáforos
        for (Semaforo semaforo : semaforos.values()) {
            semaforo.actualizarEstado();
        }
    }

    public Map<Direccion, ConcurrentLinkedQueue<Vehiculo>> getVehiculosPorDireccion() {
        return vehiculosPorDireccion;
    }

    public Map<Direccion, Semaforo> getSemaforos() {
        return semaforos;
    }
}
