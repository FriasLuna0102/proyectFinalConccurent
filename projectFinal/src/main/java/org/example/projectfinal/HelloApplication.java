package org.example.projectfinal;

import javafx.animation.AnimationTimer;
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
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.projectfinal.enumeraciones.Direccion;
import org.example.projectfinal.enumeraciones.EstadoSemaforo;
import org.example.projectfinal.enumeraciones.EstadoVehiculo;
import org.example.projectfinal.enumeraciones.TipoVehiculo;
import org.example.projectfinal.modelo.Interseccion;
import org.example.projectfinal.modelo.Semaforo;
import org.example.projectfinal.modelo.Vehiculo;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class HelloApplication extends Application {
    private Interseccion interseccion;
    private AnimationTimer animationTimer;
    private Canvas canvas;
    private GraphicsContext gc;
    private boolean simulacionIniciada = false; // Variable para rastrear si la simulación ha sido iniciada
    private Image iconoVehiculoNormal;
    private Image iconoVehiculoEmergencia;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Traffic Intersection");

        iconoVehiculoNormal = new Image(getClass().getResource("/image/car.png").toString());
        iconoVehiculoEmergencia = new Image(getClass().getResource("/image/medical.png").toString());

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER);

        Group canvasGroup = new Group();
        canvas = new Canvas(800, 600);
        gc = canvas.getGraphicsContext2D();

        // Crear controles de interfaz
        ComboBox<String> escenarioComboBox = new ComboBox<>();
        escenarioComboBox.getItems().addAll("Escenario 1", "Escenario 2");
        escenarioComboBox.setValue("Escenario 1");

        Button iniciarButton = new Button("Iniciar Simulación");
        iniciarButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");

        Label tipoVehiculoLabel = new Label("Tipo de Vehículo:");
        ComboBox<TipoVehiculo> tipoVehiculoComboBox = new ComboBox<>();
        tipoVehiculoComboBox.getItems().addAll(TipoVehiculo.NORMAL, TipoVehiculo.EMERGENCIA);
        tipoVehiculoComboBox.setValue(TipoVehiculo.NORMAL);

        Label direccionLabel = new Label("Dirección:");
        ComboBox<Direccion> direccionComboBox = new ComboBox<>();
        direccionComboBox.getItems().addAll(Direccion.DERECHA, Direccion.IZQUIERDA, Direccion.RECTO, Direccion.VUELTA_EN_U, Direccion.ARRIBA, Direccion.ABAJO);
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

        // Set scene
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void iniciarSimulacion(String escenario) {
        interseccion = new Interseccion("1");

        if ("Escenario 1".equals(escenario)) {
            // Añadir vehículos para el escenario 1
            interseccion.agregarVehiculo(Direccion.DERECHA, new Vehiculo("1", TipoVehiculo.NORMAL, Direccion.DERECHA, EstadoVehiculo.ESPERANDO, 50, 210, 0.1)); // Velocidad ajustada a la mitad
            interseccion.agregarVehiculo(Direccion.IZQUIERDA, new Vehiculo("2", TipoVehiculo.EMERGENCIA, Direccion.IZQUIERDA, EstadoVehiculo.ESPERANDO, 350, 180, 0.1)); // Velocidad ajustada a la mitad
            interseccion.agregarVehiculo(Direccion.RECTO, new Vehiculo("3", TipoVehiculo.NORMAL, Direccion.RECTO, EstadoVehiculo.ESPERANDO, 180, 50, 0.1)); // Velocidad ajustada a la mitad
            interseccion.agregarVehiculo(Direccion.VUELTA_EN_U, new Vehiculo("4", TipoVehiculo.EMERGENCIA, Direccion.VUELTA_EN_U, EstadoVehiculo.ESPERANDO, 180, 350, 0.1)); // Velocidad ajustada a la mitad
        }

        if (animationTimer != null) {
            animationTimer.stop();
        }

        // Configurar AnimationTimer para actualizar la simulación continuamente
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                interseccion.controlarSemaforos();
                dibujarInterseccion(gc);
                moverVehiculos();
            }
        };
        animationTimer.start();
    }


    private void agregarVehiculo(TipoVehiculo tipoVehiculo, Direccion direccion) {
        // Generar un ID único para el nuevo vehículo
        String id = String.valueOf(interseccion.getVehiculosPorDireccion().values().stream().mapToInt(ConcurrentLinkedQueue::size).sum() + 1);
        double posX = 0;
        double posY = 0;

        switch (direccion) {
            case DERECHA:
                posX = canvas.getWidth() + 20;
                posY = 210; // Ajustar la posición vertical según sea necesario
                break;
            case IZQUIERDA:
                posX = -20;
                posY = 180; // Ajustar la posición vertical según sea necesario
                break;
            case RECTO:
                posX = 180; // Ajustar la posición horizontal según sea necesario
                posY = -20;
                break;
            case VUELTA_EN_U:
                posX = 180; // Ajustar la posición horizontal según sea necesario
                posY = canvas.getHeight() + 20;
                break;
            case ARRIBA:
                posX = direccion == Direccion.DERECHA ? 50 : 350; // Ajustar la posición horizontal según sea necesario
                posY = -20;
                break;
            case ABAJO:
                posX =  direccion == Direccion.DERECHA ? 50 : 350; // Ajustar la posición horizontal según sea necesario
                posY = canvas.getHeight() + 20;
                break;
            default:
                break;
        }

        double velocidad = tipoVehiculo == TipoVehiculo.EMERGENCIA ? 0.1 : 0.1; // Velocidad ajustada a la mitad

        Vehiculo nuevoVehiculo = new Vehiculo(id, tipoVehiculo, direccion, EstadoVehiculo.ESPERANDO, posX, posY, velocidad);
        interseccion.agregarVehiculo(direccion, nuevoVehiculo);
    }



    private void dibujarInterseccion(GraphicsContext gc) {
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(Color.GRAY);
        gc.fillRect(150, 0, 100, canvas.getHeight());
        gc.fillRect(0, 150, canvas.getWidth(), 100);

        gc.setStroke(Color.WHITE);
        gc.setLineWidth(5);

        // Dibujar las líneas de las calles y las intersecciones
        gc.strokeLine(200, 0, 200, canvas.getHeight()); // Línea vertical central
        gc.strokeLine(0, 200, canvas.getWidth(), 200); // Línea horizontal central

        // Calle vertical
        gc.strokeLine(150, 0, 150, canvas.getHeight());
        gc.strokeLine(250, 0, 250, canvas.getHeight());

        // Calle horizontal
        gc.strokeLine(0, 150, canvas.getWidth(), 150);
        gc.strokeLine(0, 250, canvas.getWidth(), 250);

        // Dibujar los semáforos en las posiciones correctas
        gc.setFill(Color.BLACK);
        gc.fillRect(140, 185, 20, 30); // Semáforo en la calle oeste
        gc.fillRect(240, 185, 20, 30); // Semáforo en la calle este
        gc.fillRect(185, 140, 30, 20); // Semáforo en la calle norte
        gc.fillRect(185, 240, 30, 20); // Semáforo en la calle sur

        Semaforo semaforoOeste = interseccion.getSemaforos().get(Direccion.IZQUIERDA);
        Semaforo semaforoEste = interseccion.getSemaforos().get(Direccion.DERECHA);
        Semaforo semaforoNorte = interseccion.getSemaforos().get(Direccion.RECTO);
        Semaforo semaforoSur = interseccion.getSemaforos().get(Direccion.VUELTA_EN_U);

        gc.setFill(semaforoOeste.getEstado() == EstadoSemaforo.VERDE ? Color.GREEN :
                (semaforoOeste.getEstado() == EstadoSemaforo.AMARILLO ? Color.YELLOW : Color.RED));
        gc.fillOval(145, 195, 10, 10); // Semáforo en la calle oeste

        gc.setFill(semaforoEste.getEstado() == EstadoSemaforo.VERDE ? Color.GREEN :
                (semaforoEste.getEstado() == EstadoSemaforo.AMARILLO ? Color.YELLOW : Color.RED));
        gc.fillOval(245, 195, 10, 10); // Semáforo en la calle este

        gc.setFill(semaforoNorte.getEstado() == EstadoSemaforo.VERDE ? Color.GREEN :
                (semaforoNorte.getEstado() == EstadoSemaforo.AMARILLO ? Color.YELLOW : Color.RED));
        gc.fillOval(195, 145, 10, 10); // Semáforo en la calle norte

        gc.setFill(semaforoSur.getEstado() == EstadoSemaforo.VERDE ? Color.GREEN :
                (semaforoSur.getEstado() == EstadoSemaforo.AMARILLO ? Color.YELLOW : Color.RED));
        gc.fillOval(195, 245, 10, 10); // Semáforo en la calle sur
    }



    private void moverVehiculos() {
        Map<Direccion, ConcurrentLinkedQueue<Vehiculo>> vehiculosPorDireccion = interseccion.getVehiculosPorDireccion();

        // Limpiar y redibujar el fondo y las intersecciones
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(Color.GRAY);
        gc.fillRect(150, 0, 100, canvas.getHeight());
        gc.fillRect(0, 150, canvas.getWidth(), 100);

        gc.setStroke(Color.WHITE);
        gc.setLineWidth(5);

        // Dibujar las líneas de las calles y las intersecciones
        gc.strokeLine(200, 0, 200, canvas.getHeight()); // Línea vertical central
        gc.strokeLine(0, 200, canvas.getWidth(), 200); // Línea horizontal central

        // Calle vertical
        gc.strokeLine(150, 0, 150, canvas.getHeight());
        gc.strokeLine(250, 0, 250, canvas.getHeight());

        // Calle horizontal
        gc.strokeLine(0, 150, canvas.getWidth(), 150);
        gc.strokeLine(0, 250, canvas.getWidth(), 250);

        // Dibujar los semáforos en las posiciones correctas
        gc.setFill(Color.BLACK);
        gc.fillRect(140, 185, 20, 30); // Semáforo en la calle oeste
        gc.fillRect(240, 185, 20, 30); // Semáforo en la calle este
        gc.fillRect(185, 140, 30, 20); // Semáforo en la calle norte
        gc.fillRect(185, 240, 30, 20); // Semáforo en la calle sur

        Semaforo semaforoOeste = interseccion.getSemaforos().get(Direccion.IZQUIERDA);
        Semaforo semaforoEste = interseccion.getSemaforos().get(Direccion.DERECHA);
        Semaforo semaforoNorte = interseccion.getSemaforos().get(Direccion.RECTO);
        Semaforo semaforoSur = interseccion.getSemaforos().get(Direccion.VUELTA_EN_U);

        gc.setFill(semaforoOeste.getEstado() == EstadoSemaforo.VERDE ? Color.GREEN :
                (semaforoOeste.getEstado() == EstadoSemaforo.AMARILLO ? Color.YELLOW : Color.RED));
        gc.fillOval(145, 195, 10, 10); // Semáforo en la calle oeste

        gc.setFill(semaforoEste.getEstado() == EstadoSemaforo.VERDE ? Color.GREEN :
                (semaforoEste.getEstado() == EstadoSemaforo.AMARILLO ? Color.YELLOW : Color.RED));
        gc.fillOval(245, 195, 10, 10); // Semáforo en la calle este

        gc.setFill(semaforoNorte.getEstado() == EstadoSemaforo.VERDE ? Color.GREEN :
                (semaforoNorte.getEstado() == EstadoSemaforo.AMARILLO ? Color.YELLOW : Color.RED));
        gc.fillOval(195, 145, 10, 10); // Semáforo en la calle norte

        gc.setFill(semaforoSur.getEstado() == EstadoSemaforo.VERDE ? Color.GREEN :
                (semaforoSur.getEstado() == EstadoSemaforo.AMARILLO ? Color.YELLOW : Color.RED));
        gc.fillOval(195, 245, 10, 10); // Semáforo en la calle sur

        // Mover los vehículos y dibujarlos
        for (Direccion direccion : vehiculosPorDireccion.keySet()) {
            ConcurrentLinkedQueue<Vehiculo> vehiculos = vehiculosPorDireccion.get(direccion);
            Iterator<Vehiculo> iterator = vehiculos.iterator();

            while (iterator.hasNext()) {
                Vehiculo vehiculo = iterator.next();
                moverVehiculo(vehiculo);
                dibujarVehiculo(gc, vehiculo);
            }
        }
    }

    private void moverVehiculo(Vehiculo vehiculo) {
        switch (vehiculo.getDireccion()) {
            case DERECHA:
                vehiculo.setPosX(vehiculo.getPosX() + vehiculo.getVelocidad());
                if (vehiculo.getPosX() > canvas.getWidth() + 20) {
                    vehiculo.setPosX(-20);
                }
                break;
            case IZQUIERDA:
                vehiculo.setPosX(vehiculo.getPosX() - vehiculo.getVelocidad());
                if (vehiculo.getPosX() < -20) {
                    vehiculo.setPosX(canvas.getWidth() + 20);
                }
                break;
            case RECTO:
                vehiculo.setPosY(vehiculo.getPosY() - vehiculo.getVelocidad());
                if (vehiculo.getPosY() < -20) {
                    vehiculo.setPosY(canvas.getHeight() + 20);
                }
                break;
            case VUELTA_EN_U:
                vehiculo.setPosY(vehiculo.getPosY() + vehiculo.getVelocidad());
                if (vehiculo.getPosY() > canvas.getHeight() + 20) {
                    vehiculo.setPosY(-20);
                }
                break;
            default:
                break;
        }
    }

    private void dibujarVehiculo(GraphicsContext gc, Vehiculo vehiculo) {
        Image imagenVehiculo = vehiculo.getTipo() == TipoVehiculo.NORMAL ? iconoVehiculoNormal : iconoVehiculoEmergencia;
        gc.drawImage(imagenVehiculo, vehiculo.getPosX(), vehiculo.getPosY(), 20, 20);
    }


    private void actualizarEstadoSemaforos() {
        for (Direccion direccion : interseccion.getSemaforos().keySet()) {
            Semaforo semaforo = interseccion.getSemaforos().get(direccion);
            semaforo.actualizarEstado();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
