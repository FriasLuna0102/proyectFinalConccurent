package org.example.projectfinal.controladores;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import org.example.projectfinal.modelo.Interseccion;
import org.example.projectfinal.modelo.Semaforo;
import org.example.projectfinal.modelo.Vehiculo;
import org.example.projectfinal.enumeraciones.Direccion;
import org.example.projectfinal.enumeraciones.EstadoSemaforo;
import org.example.projectfinal.enumeraciones.EstadoVehiculo;
import java.util.HashMap;
import java.util.Map;

public class MainController {
    @FXML
    private BorderPane rootPane;

    @FXML
    private Canvas intersectionCanvas;

    private Map<Direccion, Interseccion> intersecciones;

    public void initialize() {
        // Inicializar las intersecciones y la simulación
        intersecciones = new HashMap<>();
        setupSimulacion();
        iniciarSimulacion();
    }

    private void setupSimulacion() {
        // Crear intersecciones de ejemplo
        Interseccion interseccion1 = new Interseccion("Interseccion 1");
        Interseccion interseccion2 = new Interseccion("Interseccion 2");
        // Agregar semáforos a las intersecciones
        interseccion1.agregarSemaforo(Direccion.DERECHA, new Semaforo());
        interseccion1.agregarSemaforo(Direccion.IZQUIERDA, new Semaforo());
        interseccion1.agregarSemaforo(Direccion.RECTO, new Semaforo());
        interseccion1.agregarSemaforo(Direccion.VUELTA_EN_U, new Semaforo());
        interseccion2.agregarSemaforo(Direccion.DERECHA, new Semaforo());
        interseccion2.agregarSemaforo(Direccion.IZQUIERDA, new Semaforo());
        interseccion2.agregarSemaforo(Direccion.VUELTA_EN_U, new Semaforo());
        interseccion2.agregarSemaforo(Direccion.RECTO, new Semaforo());
        // Agregar intersecciones al mapa
        intersecciones.put(Direccion.DERECHA, interseccion1);
        intersecciones.put(Direccion.IZQUIERDA, interseccion2);
    }

    private void iniciarSimulacion() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000); // Procesar vehículos cada segundo
                    procesarVehiculos();
                    actualizarInterfazGrafica();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void procesarVehiculos() {
        // Simular procesamiento de vehículos en cada intersección
        for (Interseccion interseccion : intersecciones.values()) {
            for (Vehiculo vehiculo : interseccion.getVehiculos()) {
                // Lógica de procesamiento de vehículos
                vehiculo.avanzar();
                // Actualizar estado del vehículo según el semáforo
                if (interseccion.getSemaforos().get(vehiculo.getDireccion()).getEstado() == EstadoSemaforo.ROJO) {
                    vehiculo.setEstado(EstadoVehiculo.DETENIDO);
                }
            }
        }
    }

    private void actualizarInterfazGrafica() {
        // Actualizar la interfaz gráfica con el estado actual de las intersecciones y vehículos
        GraphicsContext gc = intersectionCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, intersectionCanvas.getWidth(), intersectionCanvas.getHeight());

        for (Interseccion interseccion : intersecciones.values()) {
            for (Vehiculo vehiculo : interseccion.getVehiculos()) {
                // Dibujar vehículos en el canvas de la intersección
                switch (vehiculo.getEstado()) {
                    case EN_MOVIMIENTO:
                        // Dibujar vehículo en movimiento
                        break;
                    case DETENIDO:
                        // Dibujar vehículo detenido
                        break;
                    default:
                        break;
                }
            }
        }
    }

    // Métodos adicionales para manejar eventos de la interfaz gráfica y la simulación

}
