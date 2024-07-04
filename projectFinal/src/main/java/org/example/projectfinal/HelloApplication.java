package org.example.projectfinal;

import javafx.animation.AnimationTimer;
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
import org.example.projectfinal.modelo.Vehiculo;

import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {
    private List<Vehiculo> vehiculos = new ArrayList<>();

    @Override
    public void start(Stage stage) {
        stage.setTitle("Traffic Intersection");

        Group root = new Group();
        Canvas canvas = new Canvas(400, 400);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Añade algunos vehículos
        vehiculos.add(new Vehiculo("1", TipoVehiculo.NORMAL, Direccion.DERECHA, EstadoVehiculo.EN_MOVIMIENTO, 50, 200, 2));
        vehiculos.add(new Vehiculo("2", TipoVehiculo.EMERGENCIA, Direccion.IZQUIERDA, EstadoVehiculo.EN_MOVIMIENTO, 350, 200, 3));

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                dibujarInterseccion(gc);
                moverYdibujarVehiculos(gc);
            }
        }.start();

        root.getChildren().add(canvas);
        stage.setScene(new Scene(root));
        stage.show();

    }



    private void dibujarInterseccion(GraphicsContext gc) {
        // Dibujar fondo
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(0, 0, 400, 400);

        // Dibujar carreteras
        gc.setFill(Color.GRAY);
        gc.fillRect(150, 0, 100, 400); // Carretera vertical
        gc.fillRect(0, 150, 400, 100); // Carretera horizontal

        // Dibujar líneas de carril
        gc.setStroke(Color.WHITE);
        gc.setLineDashes(10);
        gc.setLineWidth(5);

        // Líneas de carril vertical
        gc.strokeLine(200, 0, 200, 150);
        gc.strokeLine(200, 250, 200, 400);

        // Líneas de carril horizontal
        gc.strokeLine(0, 200, 150, 200);
        gc.strokeLine(250, 200, 400, 200);

        // Dibujar semáforos
        gc.setFill(Color.BLACK);
        gc.fillRect(185, 140, 30, 20); // Semáforo vertical superior
        gc.fillRect(185, 240, 30, 20); // Semáforo vertical inferior
        gc.fillRect(140, 185, 20, 30); // Semáforo horizontal izquierdo
        gc.fillRect(240, 185, 20, 30); // Semáforo horizontal derecho

        // Dibujar luces rojas
        gc.setFill(Color.RED);
        gc.fillOval(195, 145, 10, 10); // Luz roja vertical superior
        gc.fillOval(195, 245, 10, 10); // Luz roja vertical inferior
        gc.fillOval(145, 195, 10, 10); // Luz roja horizontal izquierda
        gc.fillOval(245, 195, 10, 10); // Luz roja horizontal derecha
    }

    private void moverYdibujarVehiculos(GraphicsContext gc) {
        gc.clearRect(0, 0, 400, 400); // Limpiar el canvas antes de redibujar
        dibujarInterseccion(gc); // Redibujar la intersección
        for (Vehiculo vehiculo : vehiculos) {
            vehiculo.mover();
            vehiculo.dibujar(gc);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
