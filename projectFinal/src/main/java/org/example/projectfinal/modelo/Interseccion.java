package org.example.projectfinal.modelo;

import org.example.projectfinal.enumeraciones.Direccion;
import org.example.projectfinal.enumeraciones.EstadoSemaforo;
import org.example.projectfinal.enumeraciones.EstadoVehiculo;

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
        this.semaforos = new HashMap<>();
        iniciarProcesamiento();
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



    public void setSemaforos(Map<Direccion, Semaforo> semaforos) {
        this.semaforos = semaforos;
    }

    //Metodos:

    // Método para agregar un semáforo a una dirección específica
    public void agregarSemaforo(Direccion direccion, Semaforo semaforo) {
        semaforos.put(direccion, semaforo);
    }

    public synchronized void agregarVehiculo(Vehiculo vehiculo) {
        // Lógica para agregar un vehículo a la intersección
        vehiculos.add(vehiculo);
//        // Lógica para procesar el vehículo según el estado del semáforo y prioridad
//        procesarVehiculos();
    }

    public synchronized void procesarVehiculos() {
        // Ordenar los vehículos: prioridad para vehículos de emergencia
        vehiculos.sort((v1, v2) -> Boolean.compare(v2.esEmergencia(), v1.esEmergencia()));

        for (Vehiculo vehiculo : vehiculos) {
            Semaforo semaforo = semaforos.get(vehiculo.getDireccion());
            if (semaforo != null && semaforo.puedeAvanzar()) {
                vehiculo.avanzar();
                System.out.println("Vehiculo " + vehiculo.getId() + " avanzando en dirección " + vehiculo.getDireccion());
            } else {
                vehiculo.detener();
                System.out.println("Vehiculo " + vehiculo.getId() + " detenido en dirección " + vehiculo.getDireccion());
            }
            // Actualizar el estado del vehículo según el estado del semáforo
            if (semaforo != null) {
                if (semaforo.getEstado() == EstadoSemaforo.VERDE) {
                    vehiculo.setEstado(EstadoVehiculo.EN_MOVIMIENTO);
                } else if (semaforo.getEstado() == EstadoSemaforo.ROJO) {
                    vehiculo.setEstado(EstadoVehiculo.DETENIDO);
                } else if (semaforo.getEstado() == EstadoSemaforo.AMARILLO) {
                    vehiculo.setEstado(EstadoVehiculo.ESPERANDO);
                }
            }
            System.out.println("Estado actualizado del vehículo " + vehiculo.getId() + ": " + vehiculo.getEstado());
        }

        try {
            Thread.sleep(1000); // Esperar 1 segundo antes de procesar vehículos nuevamente
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    // Método para iniciar el ciclo continuo de procesamiento de vehículos
    public void iniciarProcesamiento() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000); // Procesar vehículos cada segundo
                    procesarVehiculos();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // Método para obtener una lista de semáforos
    public List<Semaforo> getSemaforos() {
        return new ArrayList<>(semaforos.values());
    }




}
