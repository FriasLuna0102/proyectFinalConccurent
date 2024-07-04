package org.example.projectfinal.modelo;

import org.example.projectfinal.enumeraciones.Direccion;
import org.example.projectfinal.enumeraciones.EstadoSemaforo;

import java.util.*;
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
            EstadoSemaforo nuevoEstado = determinarNuevoEstado(semaforo); // Método para determinar el nuevo estado según la lógica deseada
            semaforo.cambiarEstado(nuevoEstado); // Cambiar el estado del semáforo
        }
    }

    private EstadoSemaforo determinarNuevoEstado(Semaforo semaforo) {
        // Implementar lógica para determinar el nuevo estado del semáforo aquí
        // Por ejemplo, alternar entre verde y rojo, o basado en un temporizador
        switch (semaforo.getEstado()) {
            case ROJO:
                return EstadoSemaforo.VERDE;
            case VERDE:
                return EstadoSemaforo.ROJO;
            default:
                return semaforo.getEstado(); // En caso de algún estado adicional, mantener el estado actual
        }
    }

    public void gestionarCruce() {
        // Lógica para gestionar el cruce de vehículos, prioridades, evitar colisiones, etc.
    }

    public Map<Direccion, ConcurrentLinkedQueue<Vehiculo>> getVehiculosPorDireccion() {
        return vehiculosPorDireccion;
    }

    public Map<Direccion, Semaforo> getSemaforos() {
        return semaforos;
    }
}
