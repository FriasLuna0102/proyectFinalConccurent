package org.example.projectfinal;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.projectfinal.enumeraciones.Direccion;
import org.example.projectfinal.enumeraciones.EstadoSemaforo;
import org.example.projectfinal.enumeraciones.EstadoVehiculo;
import org.example.projectfinal.enumeraciones.TipoVehiculo;
import org.example.projectfinal.modelo.Interseccion;
import org.example.projectfinal.modelo.Semaforo;
import org.example.projectfinal.modelo.Vehiculo;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class HelloApplication extends Application {
    private Interseccion interseccion;
    private Timeline timeline;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Traffic Intersection");

        Group root = new Group();
        Canvas canvas = new Canvas(400, 400);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        interseccion = new Interseccion("1");

        // Añadir algunos vehículos para pruebas
        interseccion.agregarVehiculo(Direccion.DERECHA, new Vehiculo("1", TipoVehiculo.NORMAL, Direccion.DERECHA, EstadoVehiculo.ESPERANDO, 50, 210, 2));
        interseccion.agregarVehiculo(Direccion.IZQUIERDA, new Vehiculo("2", TipoVehiculo.EMERGENCIA, Direccion.IZQUIERDA, EstadoVehiculo.ESPERANDO, 350, 180, 3));

        // Configurar timeline para controlar semáforos cada 0.5 segundos
        timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(0.5),
                        event -> {
                            interseccion.controlarSemaforos();
                            dibujarInterseccion(gc);
                            moverYdibujarVehiculos(gc);
                        }
                )
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        root.getChildren().add(canvas);
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void dibujarInterseccion(GraphicsContext gc) {
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(0, 0, 400, 400);

        gc.setFill(Color.GRAY);
        gc.fillRect(150, 0, 100, 400);
        gc.fillRect(0, 150, 400, 100);

        gc.setStroke(Color.WHITE);
        gc.setLineDashes(10);
        gc.setLineWidth(5);

        gc.strokeLine(200, 0, 200, 150);
        gc.strokeLine(200, 250, 200, 400);
        gc.strokeLine(0, 200, 150, 200);
        gc.strokeLine(250, 200, 400, 200);

        // Dibujar los semáforos en las posiciones correctas
        gc.setFill(Color.BLACK);
        gc.fillRect(140, 185, 20, 30); // Semáforo en la calle oeste
        gc.fillRect(240, 185, 20, 30); // Semáforo en la calle este

        Semaforo semaforoOeste = interseccion.getSemaforos().get(Direccion.IZQUIERDA);
        Semaforo semaforoEste = interseccion.getSemaforos().get(Direccion.DERECHA);

        gc.setFill(semaforoOeste.getEstado() == EstadoSemaforo.VERDE ? Color.GREEN :
                (semaforoOeste.getEstado() == EstadoSemaforo.AMARILLO ? Color.YELLOW : Color.RED));
        gc.fillOval(145, 195, 10, 10); // Semáforo en la calle oeste

        gc.setFill(semaforoEste.getEstado() == EstadoSemaforo.VERDE ? Color.GREEN :
                (semaforoEste.getEstado() == EstadoSemaforo.AMARILLO ? Color.YELLOW : Color.RED));
        gc.fillOval(245, 195, 10, 10); // Semáforo en la calle este
    }

    private void moverYdibujarVehiculos(GraphicsContext gc) {
        gc.clearRect(0, 0, 400, 400);
        dibujarInterseccion(gc);

        Map<Direccion, ConcurrentLinkedQueue<Vehiculo>> vehiculosPorDireccion = interseccion.getVehiculosPorDireccion();
        for (Map.Entry<Direccion, ConcurrentLinkedQueue<Vehiculo>> entry : vehiculosPorDireccion.entrySet()) {
            ConcurrentLinkedQueue<Vehiculo> colaVehiculos = entry.getValue();

            for (Vehiculo vehiculo : colaVehiculos) {
                Direccion direccion = vehiculo.getDireccion();
                EstadoSemaforo estadoSemaforo = interseccion.getSemaforos().get(direccion).getEstado();

                // Verificar si el vehículo está cerca del semáforo
                if (estaCercaDelSemaforo(vehiculo) && vehiculo.getTipo() != TipoVehiculo.EMERGENCIA) {
                    if (estadoSemaforo == EstadoSemaforo.ROJO) {
                        vehiculo.detener();
                    } else if (estadoSemaforo == EstadoSemaforo.VERDE) {
                        vehiculo.reanudar();
                    }
                } else if (vehiculo.getTipo() == TipoVehiculo.EMERGENCIA) {
                    vehiculo.reanudar();
                }

                vehiculo.mover(); // Este método debería mover el vehículo si no está detenido
                vehiculo.dibujar(gc);
            }
        }
    }

    private boolean estaCercaDelSemaforo(Vehiculo vehiculo) {
        double posX = vehiculo.getPosX();
        double posY = vehiculo.getPosY();

        // Para la dirección DERECHA, detenerse en el semáforo en la calle oeste (aproximadamente posX 140, posY 200)
        if (vehiculo.getDireccion() == Direccion.DERECHA) {
            return (posX >= 130 && posX <= 150 && posY == 210);
        }

        // Para la dirección IZQUIERDA, detenerse en el semáforo en la calle este (aproximadamente posX 240, posY 200)
        if (vehiculo.getDireccion() == Direccion.IZQUIERDA) {
            return (posX >= 230 && posX <= 250 && posY == 180);
        }

        return false;
    }



    public static void main(String[] args) {
        launch(args);
    }
}
