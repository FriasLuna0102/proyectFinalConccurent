package org.example.projectfinal;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    private Canvas canvas;
    private GraphicsContext gc;
    private boolean simulacionIniciada = false; // Variable para rastrear si la simulación ha sido iniciada

    @Override
    public void start(Stage stage) {
        stage.setTitle("Traffic Intersection");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER);

        Group canvasGroup = new Group();
        canvas = new Canvas(400, 400);
        gc = canvas.getGraphicsContext2D();

        // Crear controles de interfaz
        ComboBox<String> escenarioComboBox = new ComboBox<>();
        escenarioComboBox.getItems().addAll("Escenario 1");
        escenarioComboBox.setValue("Escenario 1");

        Button iniciarButton = new Button("Iniciar Simulación");
        iniciarButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");

        Label tipoVehiculoLabel = new Label("Tipo de Vehículo:");
        ComboBox<TipoVehiculo> tipoVehiculoComboBox = new ComboBox<>();
        tipoVehiculoComboBox.getItems().addAll(TipoVehiculo.NORMAL, TipoVehiculo.EMERGENCIA);
        tipoVehiculoComboBox.setValue(TipoVehiculo.NORMAL);

        Label direccionLabel = new Label("Dirección:");
        ComboBox<Direccion> direccionComboBox = new ComboBox<>();
        direccionComboBox.getItems().addAll(Direccion.DERECHA, Direccion.IZQUIERDA);
        direccionComboBox.setValue(Direccion.DERECHA);

        Button agregarVehiculoButton = new Button("Agregar Vehículo");
        agregarVehiculoButton.setStyle("-fx-background-color: #008CBA; -fx-text-fill: white; -fx-font-size: 14px;");

        // Configurar la acción del botón iniciar
        iniciarButton.setOnAction(event -> {
            String escenarioSeleccionado = escenarioComboBox.getValue();
            iniciarSimulacion(escenarioSeleccionado);
            simulacionIniciada = true; // Actualizar la variable cuando se inicie la simulación
        });

        // Configurar la acción del botón agregar vehículo
        agregarVehiculoButton.setOnAction(event -> {
            if (!simulacionIniciada) { // Verificar si la simulación no ha sido iniciada
                Alert alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Simulación no iniciada");
                alerta.setHeaderText(null);
                alerta.setContentText("Aún no se ha iniciado la simulación. Favor iniciar la simulación.");
                alerta.showAndWait();
            } else {
                TipoVehiculo tipoVehiculo = tipoVehiculoComboBox.getValue();
                Direccion direccion = direccionComboBox.getValue();
                agregarVehiculo(tipoVehiculo, direccion);
            }
        });

        HBox controlsBox = new HBox(10, tipoVehiculoLabel, tipoVehiculoComboBox, direccionLabel, direccionComboBox, agregarVehiculoButton);
        controlsBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(escenarioComboBox, iniciarButton, controlsBox, canvasGroup);
        canvasGroup.getChildren().add(canvas);

        stage.setScene(new Scene(root));
        stage.show();
    }

    private void iniciarSimulacion(String escenario) {
        interseccion = new Interseccion("1");

        if ("Escenario 1".equals(escenario)) {
            // Añadir vehículos para el escenario 1
            interseccion.agregarVehiculo(Direccion.DERECHA, new Vehiculo("1", TipoVehiculo.NORMAL, Direccion.DERECHA, EstadoVehiculo.ESPERANDO, 50, 210, 2));
            interseccion.agregarVehiculo(Direccion.IZQUIERDA, new Vehiculo("2", TipoVehiculo.EMERGENCIA, Direccion.IZQUIERDA, EstadoVehiculo.ESPERANDO, 350, 180, 3));
        }

        if (timeline != null) {
            timeline.stop();
        }

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
    }

    private void agregarVehiculo(TipoVehiculo tipoVehiculo, Direccion direccion) {
        // Generar un ID único para el nuevo vehículo
        String id = String.valueOf(interseccion.getVehiculosPorDireccion().values().stream().mapToInt(ConcurrentLinkedQueue::size).sum() + 1);
        double posX = direccion == Direccion.DERECHA ? 50 : 350;
        double posY = direccion == Direccion.DERECHA ? 210 : 180;
//        int velocidad = tipoVehiculo == TipoVehiculo.EMERGENCIA ? 3 : 2; //Velocidad diferente para vehículos de emergencia
        int velocidad = 2; // Velocidad igual para todos los vehículos


        Vehiculo nuevoVehiculo = new Vehiculo(id, tipoVehiculo, direccion, EstadoVehiculo.ESPERANDO, posX, posY, velocidad);
        interseccion.agregarVehiculo(direccion, nuevoVehiculo);
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
            Direccion direccion = entry.getKey();
            ConcurrentLinkedQueue<Vehiculo> colaVehiculos = entry.getValue();

            Vehiculo vehiculoAnterior = null;

            for (Vehiculo vehiculo : colaVehiculos) {
                EstadoSemaforo estadoSemaforo = interseccion.getSemaforos().get(direccion).getEstado();

                // Verificar si hay un vehículo de emergencia detrás
                boolean hayEmergenciaDetras = hayVehiculoEmergenciaDetras(vehiculo, colaVehiculos);

                // Lógica de movimiento del vehículo
                if (estadoSemaforo == EstadoSemaforo.ROJO &&
                        vehiculo.getTipo() != TipoVehiculo.EMERGENCIA && // Nueva condición
                        !hayEmergenciaDetras &&
                        estaCercaDelSemaforo(vehiculo)) {
                    vehiculo.detener();
                } else if (vehiculoAnterior != null) {
                    double distancia = calcularDistancia(vehiculo, vehiculoAnterior);
                    if (distancia < 30) {
                        vehiculo.detener();
                    } else {
                        vehiculo.reanudar();
                    }
                } else {
                    vehiculo.reanudar();
                }

                vehiculo.mover();
                vehiculo.dibujar(gc);

                vehiculoAnterior = vehiculo;
            }
        }
    }

    private double calcularDistancia(Vehiculo v1, Vehiculo v2) {
        return Math.sqrt(Math.pow(v1.getPosX() - v2.getPosX(), 2) + Math.pow(v1.getPosY() - v2.getPosY(), 2));
    }

    private boolean estaCercaDelSemaforo(Vehiculo vehiculo) {
        double posX = vehiculo.getPosX();
        double posY = vehiculo.getPosY();

        if (vehiculo.getDireccion() == Direccion.DERECHA) {
            return (posX >= 130 && posX <= 150 && posY == 210);
        }

        if (vehiculo.getDireccion() == Direccion.IZQUIERDA) {
            return (posX >= 230 && posX <= 250 && posY == 180);
        }

        return false;
    }

    private boolean hayVehiculoEmergenciaDetras(Vehiculo vehiculo, ConcurrentLinkedQueue<Vehiculo> colaVehiculos) {
        boolean encontrado = false;
        for (Vehiculo v : colaVehiculos) {
            if (v == vehiculo) {
                encontrado = true;
            } else if (encontrado && v.getTipo() == TipoVehiculo.EMERGENCIA) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
