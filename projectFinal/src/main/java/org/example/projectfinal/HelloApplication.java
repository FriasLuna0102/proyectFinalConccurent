package org.example.projectfinal;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
    private boolean simulacionIniciada = false;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Traffic Intersection");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER);

        canvas = new Canvas(400, 400);
        gc = canvas.getGraphicsContext2D();

        ComboBox<String> escenarioComboBox = new ComboBox<>();
        escenarioComboBox.getItems().addAll("Escenario 1");
        escenarioComboBox.setValue("Escenario 1");

        Button iniciarButton = new Button("Iniciar Simulación");
        iniciarButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 18px;");

        Label tipoVehiculoLabel = new Label("Tipo de Vehículo:");
        ComboBox<TipoVehiculo> tipoVehiculoComboBox = new ComboBox<>();
        tipoVehiculoComboBox.getItems().addAll(TipoVehiculo.NORMAL, TipoVehiculo.EMERGENCIA);
        tipoVehiculoComboBox.setValue(TipoVehiculo.NORMAL);
        tipoVehiculoLabel.setStyle("-fx-font-size: 16px;");

        Label direccionLabel = new Label("Dirección:");
        ComboBox<Direccion> direccionComboBox = new ComboBox<>();
        direccionComboBox.getItems().addAll(Direccion.DERECHA, Direccion.IZQUIERDA, Direccion.ARRIBA, Direccion.ABAJO);
        direccionComboBox.setValue(Direccion.DERECHA);
        direccionLabel.setStyle("-fx-font-size: 16px;");

        Button agregarVehiculoButton = new Button("Agregar Vehículo");
        agregarVehiculoButton.setStyle("-fx-background-color: #008CBA; -fx-text-fill: white; -fx-font-size: 18px;");

        iniciarButton.setOnAction(event -> {
            String escenarioSeleccionado = escenarioComboBox.getValue();
            iniciarSimulacion(escenarioSeleccionado);
            simulacionIniciada = true;
        });

        agregarVehiculoButton.setOnAction(event -> {
            if (!simulacionIniciada) {
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

        root.getChildren().addAll(escenarioComboBox, iniciarButton, controlsBox, canvas);

        stage.setScene(new Scene(root));
        stage.setFullScreen(true);

        stage.show();
    }

    private void iniciarSimulacion(String escenario) {
        interseccion = new Interseccion("1");

        if ("Escenario 1".equals(escenario)) {
            //interseccion.agregarVehiculo(Direccion.DERECHA, new Vehiculo("1", TipoVehiculo.NORMAL, Direccion.DERECHA, EstadoVehiculo.ESPERANDO, 50, 210, 0.1));
            //interseccion.agregarVehiculo(Direccion.IZQUIERDA, new Vehiculo("2", TipoVehiculo.EMERGENCIA, Direccion.IZQUIERDA, EstadoVehiculo.ESPERANDO, 350, 180, 0.1));
        }

        if (timeline != null) {
            timeline.stop();
        }

        timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(0.02),
                        event -> {
                            interseccion.controlarSemaforos();
                            moverYdibujarVehiculos(gc);
                        }
                )
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void agregarVehiculo(TipoVehiculo tipoVehiculo, Direccion direccion) {
        String id = String.valueOf(interseccion.getVehiculosPorDireccion().values().stream().mapToInt(ConcurrentLinkedQueue::size).sum() + 1);
        double posX = 50;
        double posY = 50;

        switch (direccion) {
            case DERECHA:
                posX = 50;
                posY = 210;
                break;
            case IZQUIERDA:
                posX = 350;
                posY = 180;
                break;
            case ABAJO:
                posX = 190;
                posY = 0;
                break;
            case ARRIBA:
                posX = 210;
                posY = 400;
                break;
        }

        double velocidad = tipoVehiculo == TipoVehiculo.EMERGENCIA ? 0.1 : 0.1;

        Vehiculo nuevoVehiculo = new Vehiculo(id, tipoVehiculo, direccion, EstadoVehiculo.ESPERANDO, posX, posY, velocidad);
        interseccion.agregarVehiculo(direccion, nuevoVehiculo);
    }

    private void dibujarInterseccion(GraphicsContext gc) {
        gc.setFill(Color.GREEN);
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
        gc.fillRect(140, 185, 20, 30);
        gc.fillRect(240, 185, 20, 30);
        gc.fillRect(185, 140, 30, 20);
        gc.fillRect(185, 240, 30, 20);

        Semaforo semaforoOeste = interseccion.getSemaforos().get(Direccion.DERECHA);
        Semaforo semaforoEste = interseccion.getSemaforos().get(Direccion.IZQUIERDA);
        Semaforo semaforoNorte = interseccion.getSemaforos().get(Direccion.ABAJO);
        Semaforo semaforoSur = interseccion.getSemaforos().get(Direccion.ARRIBA);

        gc.setFill(semaforoOeste.getEstado() == EstadoSemaforo.VERDE ? Color.GREEN :
                (semaforoOeste.getEstado() == EstadoSemaforo.AMARILLO ? Color.YELLOW : Color.RED));
        gc.fillOval(145, 195, 10, 10);

        gc.setFill(semaforoEste.getEstado() == EstadoSemaforo.VERDE ? Color.GREEN :
                (semaforoEste.getEstado() == EstadoSemaforo.AMARILLO ? Color.YELLOW : Color.RED));
        gc.fillOval(245, 195, 10, 10);

        gc.setFill(semaforoNorte.getEstado() == EstadoSemaforo.VERDE ? Color.GREEN :
                (semaforoNorte.getEstado() == EstadoSemaforo.AMARILLO ? Color.YELLOW : Color.RED));
        gc.fillOval(195, 145, 10, 10);

        gc.setFill(semaforoSur.getEstado() == EstadoSemaforo.VERDE ? Color.GREEN :
                (semaforoSur.getEstado() == EstadoSemaforo.AMARILLO ? Color.YELLOW : Color.RED));
        gc.fillOval(195, 245, 10, 10);
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

                boolean hayEmergenciaDetras = hayVehiculoEmergenciaDetras(vehiculo, colaVehiculos);

                if (estadoSemaforo == EstadoSemaforo.ROJO &&
                        vehiculo.getTipo() != TipoVehiculo.EMERGENCIA &&
                        !hayEmergenciaDetras &&
                        estaCercaDelSemaforo(vehiculo)) {
                    vehiculo.detener();
                } else if (vehiculoAnterior != null) {
                    double distancia = calcularDistancia(vehiculo, vehiculoAnterior);
                    if (distancia < 40) {
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

        if (vehiculo.getDireccion() == Direccion.ABAJO) {
            return (posY >= 130 && posY <= 150 && posX == 190);
        }

        if (vehiculo.getDireccion() == Direccion.ARRIBA) {
            return (posY >= 250 && posY <= 270 && posX == 210);
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
