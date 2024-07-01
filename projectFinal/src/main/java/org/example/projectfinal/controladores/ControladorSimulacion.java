package org.example.projectfinal.controladores;

import org.example.projectfinal.enumeraciones.Direccion;
import org.example.projectfinal.enumeraciones.EstadoSemaforo;
import org.example.projectfinal.modelo.Interseccion;
import org.example.projectfinal.modelo.Semaforo;
import org.example.projectfinal.modelo.Vehiculo;

import java.util.ArrayList;
import java.util.List;

public class ControladorSimulacion {
    private List<Interseccion> intersecciones;

    public ControladorSimulacion() {
        // Inicializar las intersecciones y semáforos
        intersecciones = new ArrayList<>();

        // Ejemplo de inicialización de una intersección
        Interseccion interseccion = new Interseccion("1");
        interseccion.agregarSemaforo(Direccion.DERECHA, new Semaforo("1", EstadoSemaforo.ROJO, 5000, 10000, 2000));
        interseccion.agregarSemaforo(Direccion.RECTO, new Semaforo("2", EstadoSemaforo.ROJO, 5000, 10000, 2000));
        interseccion.agregarSemaforo(Direccion.IZQUIERDA, new Semaforo("3", EstadoSemaforo.ROJO, 5000, 10000, 2000));
        interseccion.agregarSemaforo(Direccion.VUELTA_EN_U, new Semaforo("4", EstadoSemaforo.ROJO, 5000, 10000, 2000));
        intersecciones.add(interseccion);
    }

    public void iniciarSimulacion() {
        // Lógica para iniciar la simulación
        for (Interseccion interseccion : intersecciones) {
            // Iniciar el ciclo de los semáforos
            for (Semaforo semaforo : interseccion.getSemaforos()) {
                new Thread(() -> semaforo.iniciarCiclo()).start();
            }

            // Iniciar el procesamiento de vehículos en la intersección
            new Thread(() -> interseccion.iniciarProcesamiento()).start();
        }
    }

    public void generarVehiculo(String idInterseccion, Vehiculo vehiculo) {
        // Lógica para generar un vehículo en una intersección específica
        for (Interseccion interseccion : intersecciones) {
            if (interseccion.getId().equals(idInterseccion)) {
                interseccion.agregarVehiculo(vehiculo);
                break;
            }
        }
    }
}
