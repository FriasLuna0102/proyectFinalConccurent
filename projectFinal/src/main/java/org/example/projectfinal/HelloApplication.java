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
import org.example.projectfinal.enumeraciones.*;
import org.example.projectfinal.modelo.Interseccion;
import org.example.projectfinal.modelo.Semaforo;
import org.example.projectfinal.modelo.Vehiculo;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class HelloApplication extends Application {
    private Interseccion interseccion;
    private Timeline timeline;
    private Canvas canvas;
    private GraphicsContext gc;
    private boolean simulacionIniciada = false;
    private Queue<Vehiculo> vehiculosEnInterseccion;
    private Button agregarVehiculoButton; //
    private Timeline validacionTimeline;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Traffic Intersection");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER);
        vehiculosEnInterseccion = new LinkedList<>();

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
        direccionComboBox.getItems().addAll(Direccion.IZQUIERDA, Direccion.DERECHA, Direccion.ABAJO, Direccion.ARRIBA);
        direccionComboBox.setValue(Direccion.DERECHA);
        direccionLabel.setStyle("-fx-font-size: 16px;");

        Label accionLabel = new Label("Acción:");
        ComboBox<Accion> accionComboBox = new ComboBox<>();
        accionComboBox.getItems().addAll(Accion.values());
        accionComboBox.setValue(Accion.SEGUIR_RECTO);
        accionLabel.setStyle("-fx-font-size: 16px;");


        agregarVehiculoButton = new Button("Agregar Vehículo");
        agregarVehiculoButton.setStyle("-fx-background-color: #008CBA; -fx-text-fill: white; -fx-font-size: 18px;");
        agregarVehiculoButton.setDisable(true); // Inicialmente deshabilitado

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
                Accion accion = accionComboBox.getValue();
                agregarVehiculo(tipoVehiculo, direccion, accion);
            }
        });

        HBox controlsBox = new HBox(10, tipoVehiculoLabel, tipoVehiculoComboBox, direccionLabel, direccionComboBox, accionLabel, accionComboBox, agregarVehiculoButton);
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
            //interseccion.agregarVehiculo(Direccion.IZQUIERDA, new Vehiculo("2", TipoVehiculo.EMERGENCIA, Direccion.IZQUIERDA, EstadoVehiculo.ESPERANDO, 350,
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

        // Inicialización y reinicio del validacionTimeline
        validacionTimeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(0.1),
                        event -> {
                            ComboBox<Direccion> direccionComboBox = (ComboBox<Direccion>) ((HBox) agregarVehiculoButton.getParent()).getChildren().get(3);
                            Direccion direccion = direccionComboBox.getValue();
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

                            // Habilitar o deshabilitar el botón según la validez de la posición
                            if (esPosicionValida(posX, posY, direccion)) {
                                agregarVehiculoButton.setDisable(false);
                            } else {
                                agregarVehiculoButton.setDisable(true);
                            }
                        }
                )
        );
        validacionTimeline.setCycleCount(Timeline.INDEFINITE);
        validacionTimeline.play();
    }

    private void agregarVehiculo(TipoVehiculo tipoVehiculo, Direccion direccion, Accion accion) {
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

        // Verificar que no haya otro vehículo en la posición inicial propuesta
        if (!esPosicionValida(posX, posY, direccion)) {
            return; // No agrega el vehículo si la posición no es válida
        }

        double velocidad = tipoVehiculo == TipoVehiculo.EMERGENCIA ? 0.2 : 0.2;

        Vehiculo nuevoVehiculo = new Vehiculo(id, tipoVehiculo, direccion, EstadoVehiculo.ESPERANDO, posX, posY, velocidad, accion);
        interseccion.agregarVehiculo(direccion, nuevoVehiculo);
    }

    private boolean esPosicionValida(double posX, double posY, Direccion direccion) {
        ConcurrentLinkedQueue<Vehiculo> colaVehiculos = interseccion.getVehiculosPorDireccion().get(direccion);

        if (colaVehiculos == null) {
            return true; // No hay vehículos en esta dirección, posición válida
        }

        for (Vehiculo vehiculo : colaVehiculos) {
            double distancia = calcularDistancia(vehiculo.getPosX(), vehiculo.getPosY(), posX, posY);
            if (distancia < 40) {
                return false; // La distancia mínima no se cumple
            }
        }

        return true; // La posición es válida
    }

    private double calcularDistancia(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
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

                if (vehiculo.getTipo() == TipoVehiculo.EMERGENCIA) {
                    procesarVehiculoEmergencia(vehiculo, vehiculoAnterior);
                } else {
                    procesarVehiculoNormal(vehiculo, estadoSemaforo, hayEmergenciaDetras, vehiculoAnterior);
                }

                if (estaEnInterseccion(vehiculo)) {
                    aplicarAccion(vehiculo);
                }

                vehiculo.mover();
                vehiculo.dibujar(gc);

                vehiculoAnterior = vehiculo;
            }
        }
    }

    private void procesarVehiculoEmergencia(Vehiculo vehiculo, Vehiculo vehiculoAnterior) {
        if (!vehiculosEnInterseccion.isEmpty() && vehiculosEnInterseccion.peek() != vehiculo) {
            if (estaEnInterseccion(vehiculo)) {
                vehiculo.detener();
            }
        } else {
            vehiculo.reanudar();
            actualizarVehiculoEnInterseccion(vehiculo);
        }

        if (vehiculoAnterior != null && calcularDistancia(vehiculo, vehiculoAnterior) < 40) {
            vehiculo.detener();
        }
    }

    private void procesarVehiculoNormal(Vehiculo vehiculo, EstadoSemaforo estadoSemaforo, boolean hayEmergenciaDetras, Vehiculo vehiculoAnterior) {
        actualizarVehiculoEnInterseccion(vehiculo);

        if (estadoSemaforo == EstadoSemaforo.ROJO && !hayEmergenciaDetras && estaCercaDelSemaforo(vehiculo)) {
            vehiculo.detener();
        } else if (vehiculoAnterior != null) {
            manejarDistanciaConAnterior(vehiculo, vehiculoAnterior);
        } else {
            manejarSituacionSinAnterior(vehiculo, hayEmergenciaDetras);
        }
    }

    private void actualizarVehiculoEnInterseccion(Vehiculo vehiculo) {
        if (estaEnInterseccion(vehiculo)) {
            if (!vehiculosEnInterseccion.contains(vehiculo)) {
                vehiculosEnInterseccion.add(vehiculo);
            }
        } else {
            vehiculosEnInterseccion.remove(vehiculo);
        }
    }

    private void manejarDistanciaConAnterior(Vehiculo vehiculo, Vehiculo vehiculoAnterior) {
        double distancia = calcularDistancia(vehiculo, vehiculoAnterior);
        if (hayVehiculoEmergenciaEnInterseccion() && estaCercaVehiculoEmergencia(vehiculo)) {
            vehiculo.detener();
        } else if (distancia < 40) {
            vehiculo.detener();
        } else {
            vehiculo.reanudar();
        }
    }

    private void manejarSituacionSinAnterior(Vehiculo vehiculo, boolean hayEmergenciaDetras) {
        boolean hayVehiculosEnDireccionDiferente = vehiculosEnInterseccion.stream()
                .anyMatch(v -> v.getDireccion() != vehiculo.getDireccion());

        if (hayEmergenciaDetras || (hayVehiculoEmergenciaEnInterseccion() && estaCercaVehiculoEmergencia(vehiculo))) {
            if (vehiculosEnInterseccion.isEmpty() || !hayVehiculosEnDireccionDiferente) {
                vehiculo.reanudar();
            } else {
                System.out.println("cant: " + vehiculosEnInterseccion.size());
                vehiculo.detener();
            }
        } else {
            vehiculo.reanudar();
        }
    }

    private void aplicarAccion(Vehiculo vehiculo) {
        if (vehiculo.getAccionAplicada()) {
            return; // La acción ya se aplicó, no hacer nada
        }

        switch (vehiculo.getAccion()) {
            case DOBLAR_DERECHA:
                doblarDerecha(vehiculo);
                break;
            case DOBLAR_IZQUIERDA:
                doblarIzquierda(vehiculo);
                break;
            case GIRAR_U:
                girarU(vehiculo);
                break;
            case SEGUIR_RECTO:
            default:
                // No es necesario hacer nada especial para seguir recto
                break;
        }
        if (!estaEnInterseccion(vehiculo)) {
            vehiculo.setAccionAplicada(false);
        }
        vehiculo.setAccionAplicada(true);
    }

    private void doblarDerecha(Vehiculo vehiculo) {
        switch (vehiculo.getDireccion()) {
            case DERECHA:
                vehiculo.setDireccion(Direccion.ABAJO);
                vehiculo.setPosX(180);  // Ajustar a la posición correcta en la intersección
                vehiculo.setPosY(210);  // Ajustar a la posición correcta en la intersección
                break;
            case IZQUIERDA:
                vehiculo.setDireccion(Direccion.ARRIBA);
                vehiculo.setPosX(210);  // Ajustar a la posición correcta en la intersección
                vehiculo.setPosY(180);  // Ajustar a la posición correcta en la intersección
                break;
            case ABAJO:
                vehiculo.setDireccion(Direccion.IZQUIERDA);
                vehiculo.setPosX(180);  // Ajustar a la posición correcta en la intersección
                vehiculo.setPosY(180);  // Ajustar a la posición correcta en la intersección
                break;
            case ARRIBA:
                vehiculo.setDireccion(Direccion.DERECHA);
                vehiculo.setPosX(200);  // Ajustar a la posición correcta en la intersección
                vehiculo.setPosY(210);  // Ajustar a la posición correcta en la intersección
                break;
        }
    }

    private void doblarIzquierda(Vehiculo vehiculo) {
        switch (vehiculo.getDireccion()) {
            case DERECHA:
                vehiculo.setDireccion(Direccion.ARRIBA);
                vehiculo.setPosX(210);  // Ajustar a la posición correcta en la intersección
                vehiculo.setPosY(180);  // Ajustar a la posición correcta en la intersección
                break;
            case IZQUIERDA:
                vehiculo.setDireccion(Direccion.ABAJO);
                vehiculo.setPosX(180);  // Ajustar a la posición correcta en la intersección
                vehiculo.setPosY(210);  // Ajustar a la posición correcta en la intersección
                break;
            case ABAJO:
                vehiculo.setDireccion(Direccion.DERECHA);
                vehiculo.setPosX(200);  // Ajustar a la posición correcta en la intersección
                vehiculo.setPosY(210);  // Ajustar a la posición correcta en la intersección
                break;
            case ARRIBA:
                vehiculo.setDireccion(Direccion.IZQUIERDA);
                vehiculo.setPosX(180);  // Ajustar a la posición correcta en la intersección
                vehiculo.setPosY(180);  // Ajustar a la posición correcta en la intersección
                break;
        }
    }


    // Método para girar el vehículo y actualizar la interfaz gráfica
    private void girarU(Vehiculo vehiculo) {
        // Guardar la dirección anterior antes de cambiarla
        Direccion direccionAnterior = vehiculo.getDireccion();

        // Configurar el Timeline para la animación del giro
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.2), event -> {
                    switch (direccionAnterior) {
                        case DERECHA:
                            vehiculo.setDireccion(Direccion.IZQUIERDA);
                            vehiculo.setPosX(250);  // Ajustar posición X después del giro
                            break;
                        case IZQUIERDA:
                            vehiculo.setDireccion(Direccion.DERECHA);
                            vehiculo.setPosX(150);  // Ajustar posición X después del giro
                            break;
                        case ARRIBA:
                            vehiculo.setDireccion(Direccion.ABAJO);
                            vehiculo.setPosY(250);  // Ajustar posición Y después del giro
                            break;
                        case ABAJO:
                            vehiculo.setDireccion(Direccion.ARRIBA);
                            vehiculo.setPosY(150);  // Ajustar posición Y después del giro
                            break;
                    }
                    ajustarPosicionDespuesDeGiroU(vehiculo);  // Ajustar posición final después del giro
                }),
                new KeyFrame(Duration.seconds(1))  // Duración de la animación (1 segundo, ajusta según necesites)
        );

        timeline.play();  // Iniciar la animación
    }

    // Método para ajustar la posición después del giro
    private void ajustarPosicionDespuesDeGiroU(Vehiculo vehiculo) {
        switch (vehiculo.getDireccion()) {
            case ARRIBA:
                vehiculo.setPosX(210);  // Ajustar a la posición correcta en la calle vertical
                vehiculo.setPosY(150);   // Colocar en la posición correcta para moverse hacia arriba
                break;
            case ABAJO:
                vehiculo.setPosX(190);  // Ajustar a la posición correcta en la calle vertical
                vehiculo.setPosY(250);  // Colocar en la posición correcta para moverse hacia abajo
                break;
            case DERECHA:
                vehiculo.setPosX(250);  // Colocar en la posición correcta para moverse hacia la derecha
                vehiculo.setPosY(210);  // Ajustar a la posición correcta en la calle horizontal
                break;
            case IZQUIERDA:
                vehiculo.setPosX(120);   // Colocar en la posición correcta para moverse hacia la izquierda
                vehiculo.setPosY(180);  // Ajustar a la posición correcta en la calle horizontal
                break;
        }
    }



    private boolean hayVehiculoEmergenciaEnInterseccion() {
        for (Vehiculo vehiculo : vehiculosEnInterseccion) {
            if (vehiculo.getTipo() == TipoVehiculo.EMERGENCIA) {
                return true;
            }
        }
        return false;
    }

    private boolean estaCercaVehiculoEmergencia(Vehiculo vehiculo) {
        for (Vehiculo vehiculoEnInterseccion : vehiculosEnInterseccion) {
            if (vehiculoEnInterseccion.getTipo() == TipoVehiculo.EMERGENCIA) {
                double distancia = calcularDistancia(vehiculo, vehiculoEnInterseccion);
                if (distancia < 40) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean estaEnInterseccion(Vehiculo vehiculo) {
        double posX = vehiculo.getPosX();
        double posY = vehiculo.getPosY();

        return (posX >= 150 && posX <= 250 && posY >= 150 && posY <= 250);
    }

    private double calcularDistancia(Vehiculo v1, Vehiculo v2) {
        return Math.sqrt(Math.pow(v1.getPosX() - v2.getPosX(), 2) + Math.pow(v1.getPosY() - v2.getPosY(), 2));
    }


    // Método para verificar si hay un vehículo de emergencia detrás
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

    private boolean estaCercaDelSemaforo(Vehiculo vehiculo) {
        double posX = vehiculo.getPosX();
        double posY = vehiculo.getPosY();
        int margenDetencion = 20; // Ajusta este valor según sea necesario

        switch (vehiculo.getDireccion()) {
            case DERECHA:
                return (posX >= 130 - margenDetencion && posX <= 150 && posY >= 200 && posY <= 220);
            case IZQUIERDA:
                return (posX >= 230 && posX <= 250 + margenDetencion && posY >= 170 && posY <= 190);
            case ABAJO:
                return (posY >= 130 - margenDetencion && posY <= 150 && posX >= 180 && posX <= 200);
            case ARRIBA:
                return (posY >= 250 && posY <= 270 + margenDetencion && posX >= 200 && posX <= 220);
            default:
                return false;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
