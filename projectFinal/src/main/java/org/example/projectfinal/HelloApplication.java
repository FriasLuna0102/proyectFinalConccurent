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
import org.example.projectfinal.enumeraciones.Direccion;
import org.example.projectfinal.enumeraciones.EstadoVehiculo;
import org.example.projectfinal.enumeraciones.TipoVehiculo;
import org.example.projectfinal.modelo.Interseccion;
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
        interseccion.agregarVehiculo(Direccion.DERECHA, new Vehiculo("1", TipoVehiculo.NORMAL, Direccion.DERECHA, EstadoVehiculo.ESPERANDO, 50, 200, 2));
        interseccion.agregarVehiculo(Direccion.IZQUIERDA, new Vehiculo("2", TipoVehiculo.EMERGENCIA, Direccion.IZQUIERDA, EstadoVehiculo.ESPERANDO, 350, 200, 3));

        // Configurar timeline para controlar semáforos cada 2 segundos
        timeline = new Timeline(
                new KeyFrame(
                        javafx.util.Duration.seconds(2),
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

        gc.setFill(Color.BLACK);
        gc.fillRect(185, 140, 30, 20);
        gc.fillRect(185, 240, 30, 20);
        gc.fillRect(140, 185, 20, 30);
        gc.fillRect(240, 185, 20, 30);

        gc.setFill(Color.RED);
        gc.fillOval(195, 145, 10, 10);
        gc.fillOval(195, 245, 10, 10);
        gc.fillOval(145, 195, 10, 10);
        gc.fillOval(245, 195, 10, 10);
    }

    private void moverYdibujarVehiculos(GraphicsContext gc) {
        gc.clearRect(0, 0, 400, 400);
        dibujarInterseccion(gc);

        Map<Direccion, ConcurrentLinkedQueue<Vehiculo>> vehiculosPorDireccion = interseccion.getVehiculosPorDireccion();
        for (Map.Entry<Direccion, ConcurrentLinkedQueue<Vehiculo>> entry : vehiculosPorDireccion.entrySet()) {
            Direccion direccion = entry.getKey();
            ConcurrentLinkedQueue<Vehiculo> colaVehiculos = entry.getValue();

            for (Vehiculo vehiculo : colaVehiculos) {
                vehiculo.dibujar(gc);
                vehiculo.mover(); // Este método debería mover el vehículo
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
