package org.example.projectfinal.escenario2;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.example.projectfinal.enumeraciones.*;
import org.example.projectfinal.modelo.Interseccion;
import org.example.projectfinal.modelo.Semaforo;
import org.example.projectfinal.modelo.Vehiculo;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

import static org.example.projectfinal.enumeraciones.EstadoSemaforo.*;

public class Escenario2 {
    private List<Interseccion> intersecciones;
    private Canvas canvas;
    private GraphicsContext gc;
    private Map<Interseccion, Map<Direccion, List<Carril>>> carrilesPorInterseccion;
    private Map<Interseccion, Integer> tiempoSemaforos; // Tiempo restante para cada semáforo
    private Map<Interseccion, Instant> ultimoCambioSemaforo;
    private Map<Interseccion, EstadoSemaforo> estadoActualSemaforo;
    private static final Duration DURACION_VERDE = Duration.ofSeconds(10);
    private static final Duration DURACION_ROJO = Duration.ofSeconds(10);

    public Escenario2() {
        intersecciones = new ArrayList<>();
        for (int i = 0; i < 6; i++) {  // Aumentado a 6 intersecciones
            intersecciones.add(new Interseccion(String.valueOf(i + 1)));
        }
        canvas = new Canvas(1200, 800);  // Aumentado el alto del canvas
        gc = canvas.getGraphicsContext2D();
        inicializarCarriles();
        inicializarTemporizadoresSemaforos();
    }


    private void inicializarTemporizadoresSemaforos() {
        ultimoCambioSemaforo = new HashMap<>();
        estadoActualSemaforo = new HashMap<>();
        Instant ahora = Instant.now();
        for (Interseccion interseccion : intersecciones) {
            ultimoCambioSemaforo.put(interseccion, ahora);
            estadoActualSemaforo.put(interseccion, EstadoSemaforo.VERDE);
            // Inicializar todos los semáforos en rojo excepto uno
            for (Semaforo semaforo : interseccion.getSemaforos().values()) {
                semaforo.cambiarEstado(EstadoSemaforo.ROJO);
            }
            Direccion direccionInicial = Direccion.values()[0];
            interseccion.getSemaforos().get(direccionInicial).cambiarEstado(EstadoSemaforo.VERDE);
            interseccion.setDireccionVerde(direccionInicial);
        }
    }

    public void controlarSemaforos() {
        Instant ahora = Instant.now();
        for (Interseccion interseccion : intersecciones) {
            Instant ultimoCambio = ultimoCambioSemaforo.get(interseccion);
            Duration tiempoTranscurrido = Duration.between(ultimoCambio, ahora);
            EstadoSemaforo estadoActual = estadoActualSemaforo.get(interseccion);

            if (estadoActual == EstadoSemaforo.VERDE && tiempoTranscurrido.compareTo(DURACION_VERDE) >= 0) {
                cambiarAEstadoRojo(interseccion, ahora);
            } else if (estadoActual == EstadoSemaforo.ROJO && tiempoTranscurrido.compareTo(DURACION_ROJO) >= 0) {
                cambiarAEstadoVerde(interseccion, ahora);
            }
        }
    }

    private void cambiarAEstadoRojo(Interseccion interseccion, Instant ahora) {
        Direccion direccionActual = interseccion.getDireccionVerde();
        interseccion.getSemaforos().get(direccionActual).cambiarEstado(EstadoSemaforo.ROJO);
        estadoActualSemaforo.put(interseccion, EstadoSemaforo.ROJO);
        ultimoCambioSemaforo.put(interseccion, ahora);
        System.out.println("Cambiando semáforo en intersección " + interseccion.getId() + " a ROJO");
    }

    private void cambiarAEstadoVerde(Interseccion interseccion, Instant ahora) {
        Direccion direccionActual = interseccion.getDireccionVerde();
        Direccion[] direcciones = Direccion.values();
        Direccion nuevaDireccion;

        if (direccionActual == null) {
            // Si no hay dirección verde actual, elegimos la primera dirección
            nuevaDireccion = direcciones[0];
        } else {
            int index = (direccionActual.ordinal() + 1) % direcciones.length;
            nuevaDireccion = direcciones[index];

            // Cambiamos el semáforo actual a rojo
            Semaforo semaforoActual = interseccion.getSemaforos().get(direccionActual);
            if (semaforoActual != null) {
                semaforoActual.cambiarEstado(EstadoSemaforo.ROJO);
            }
        }

        // Cambiamos el nuevo semáforo a verde
        Semaforo nuevoSemaforo = interseccion.getSemaforos().get(nuevaDireccion);
        if (nuevoSemaforo != null) {
            nuevoSemaforo.cambiarEstado(EstadoSemaforo.VERDE);
        }

        interseccion.setDireccionVerde(nuevaDireccion);
        estadoActualSemaforo.put(interseccion, EstadoSemaforo.VERDE);
        ultimoCambioSemaforo.put(interseccion, ahora);

        System.out.println("Cambiando semáforo en intersección " + interseccion.getId() +
                ": " + (direccionActual != null ? direccionActual + " a ROJO, " : "") +
                nuevaDireccion + " a VERDE");
    }


    public boolean esPosicionValida(int interseccionIndex, Direccion direccion) {
        Interseccion interseccion = intersecciones.get(interseccionIndex);
        Map<Direccion, List<Carril>> carriles = carrilesPorInterseccion.get(interseccion);
        List<Carril> carrilesDireccion = carriles.get(direccion);

        for (Carril carril : carrilesDireccion) {
            if (carril.tieneEspacioDisponible()) {
                return true;
            }
        }
        return false;
    }

    public void iniciar() {
        System.out.println("Escenario 2 iniciado con " + intersecciones.size() + " intersecciones.");
    }
        // Aquí puedes añadir más lógica de inicialización si es necesario


    private void inicializarCarriles() {
        carrilesPorInterseccion = new HashMap<>();
        for (Interseccion interseccion : intersecciones) {
            Map<Direccion, List<Carril>> carrilesPorDireccion = new EnumMap<>(Direccion.class);
            for (Direccion direccion : Direccion.values()) {
                List<Carril> carriles = new ArrayList<>();
                carriles.add(new Carril(TipoCarril.IZQUIERDA));
                carriles.add(new Carril(TipoCarril.CENTRO));
                carriles.add(new Carril(TipoCarril.DERECHA));
                carrilesPorDireccion.put(direccion, carriles);
            }
            carrilesPorInterseccion.put(interseccion, carrilesPorDireccion);
        }
    }


    public void dibujar() {
        gc.clearRect(0, 0, 1200, 800);
        dibujarCalles();
        controlarSemaforos();  // Asegurarse de que esto se llame en cada frame
        dibujarIntersecciones();
        moverYdibujarVehiculos();
        dibujarVehiculos();
        moverYdibujarVehiculosInferiores();
        dibujarVehiculosInferiores();
    }

    private void dibujarCalles() {
        gc.setFill(Color.GRAY);
        gc.fillRect(0, 100, 1200, 100); // Calle horizontal superior
        gc.fillRect(0, 400, 1200, 100); // Calle horizontal inferior
        for (int i = 0; i < 3; i++) {
            gc.fillRect(200 + i * 400, 0, 100, 600); // Calles verticales
        }

        gc.setLineWidth(2);
        gc.setStroke(Color.WHITE);
        gc.strokeLine(0, 130, 1200, 130); // Línea que divide el carril superior
        gc.strokeLine(0, 170, 1200, 170); // Línea que divide el segundo carril superior
        gc.strokeLine(0, 430, 1200, 430); // Línea que divide el carril inferior
        gc.strokeLine(0, 470, 1200, 470); // Línea que divide el segundo carril inferior
        for (int i = 0; i < 3; i++) {
            gc.strokeLine(250 + i * 400, 0, 250 + i * 400, 600); // Línea que divide los carriles verticales
        }
    }

    private void dibujarIntersecciones() {
        for (int i = 0; i < 3; i++) {
            Interseccion interseccionSuperior = intersecciones.get(i);
            Interseccion interseccionInferior = intersecciones.get(i + 3);

            double xSuperior = 250 + i * 400;
            double ySuperior = 150;
            double xInferior = 250 + i * 400;
            double yInferior = 450;

            interseccionSuperior.setPosX(xSuperior);
            interseccionSuperior.setPosY(ySuperior);
            interseccionInferior.setPosX(xInferior);
            interseccionInferior.setPosY(yInferior);

            dibujarInterseccion(xSuperior, ySuperior, interseccionSuperior);
            dibujarInterseccion(xInferior, yInferior, interseccionInferior);
        }
    }

    private void dibujarInterseccion(double x, double y, Interseccion interseccion) {
        gc.setFill(Color.BLACK);
        gc.fillRect(x - 10, y - 15, 20, 30);

        EstadoSemaforo estadoActual = estadoActualSemaforo.get(interseccion);
        Color colorSemaforo = (estadoActual == EstadoSemaforo.VERDE) ? Color.GREEN : Color.RED;

        gc.setFill(colorSemaforo);
        gc.fillOval(x - 5, y - 10, 10, 10);

        // Añadir información de depuración
        System.out.println("Intersección: " + interseccion.getId() +
                ", Dirección verde: " + interseccion.getDireccionVerde() +
                ", Estado: " + estadoActual);
    }

    private void dibujarVehiculosInferiores() {
        for (Map.Entry<Interseccion, Map<Direccion, List<Carril>>> interseccionEntry : carrilesPorInterseccion.entrySet()) {
            Interseccion interseccion = interseccionEntry.getKey();
            Map<Direccion, List<Carril>> direccionCarriles = interseccionEntry.getValue();
            for (List<Carril> carriles : direccionCarriles.values()) {
                for (Carril carril : carriles) {
                    for (Vehiculo vehiculo : carril.getVehiculosInferiores()) {
                        dibujarVehiculoInferiores(vehiculo, intersecciones.indexOf(interseccion));
                    }
                }
            }
        }
    }

    private void dibujarVehiculos() {
        for (Map.Entry<Interseccion, Map<Direccion, List<Carril>>> interseccionEntry : carrilesPorInterseccion.entrySet()) {
            Interseccion interseccion = interseccionEntry.getKey();
            Map<Direccion, List<Carril>> direccionCarriles = interseccionEntry.getValue();
            for (List<Carril> carriles : direccionCarriles.values()) {
                for (Carril carril : carriles) {
                    for (Vehiculo vehiculo : carril.getVehiculos()) {
                        dibujarVehiculo(vehiculo, intersecciones.indexOf(interseccion));
                    }
                }
            }
        }
    }

    private void dibujarVehiculo(Vehiculo vehiculo, int interseccionIndex) {
        gc.save(); // Guardar el estado actual del contexto gráfico

        double offsetY = interseccionIndex >= 3 ? 300 : 0;  // Ajuste para vehículos en la calle inferior
        double posX = vehiculo.getPosX();
        double posY = vehiculo.getPosY() + offsetY;

        gc.setFill(vehiculo.getTipo() == TipoVehiculo.EMERGENCIA ? Color.RED : Color.BLUE);

        switch (vehiculo.getDireccion()) {
            case DERECHA:
                gc.fillOval(posX, posY, 20, 10);
                break;
            case IZQUIERDA:
                gc.fillOval(posX, posY, 20, 10);
                break;
            case ARRIBA:
                gc.fillOval(posX, posY, 10, 20);
                break;
            case ABAJO:
                gc.fillOval(posX, posY, 10, 20);
                break;
        }

        gc.restore(); // Restaurar el estado del contexto gráfico
    }




    private void dibujarVehiculoInferiores(Vehiculo vehiculo, int interseccionIndex) {
        gc.save(); // Guardar el estado actual del contexto gráfico

        double offsetY = interseccionIndex >= 3 ? 300 : 0;  // Ajuste para vehículos en la calle inferior
        double posXX = vehiculo.getPosXX();
        double posYY = vehiculo.getPosYY() + offsetY;

        gc.setFill(vehiculo.getTipo() == TipoVehiculo.EMERGENCIA ? Color.RED : Color.BLUE);

        switch (vehiculo.getDireccion()) {
            case DERECHA:
                gc.fillOval(posXX, posYY, 20, 10);
            case IZQUIERDA:
                gc.fillOval(posXX, posYY, 20, 10);
                break;
            case ABAJO:
                gc.translate(posYY - 135 , posXX + 150); // Trasladar al centro del vehículo
                gc.rotate(180);
                gc.fillOval(-10, -5, 20, 10);
                break;
            case ARRIBA:
                gc.translate(posXX + 135, posYY + 150); // Trasladar al centro del vehículo
                gc.rotate(-90);
                gc.fillOval(-10, -5, 20, 10);
                break;
        }
        gc.restore(); // Restaurar el estado original del contexto gráfico
    }


    private Carril seleccionarCarrilAdecuado(List<Carril> carriles, Accion accion) {
        for (Carril carril : carriles) {
            if (carril.puedeRealizarAccion(accion)) {
                return carril;
            }
        }
        return null;
    }

    private void moverYdibujarVehiculos() {
        for (Map.Entry<Interseccion, Map<Direccion, List<Carril>>> interseccionEntry : carrilesPorInterseccion.entrySet()) {
            Interseccion interseccion = interseccionEntry.getKey();
            Map<Direccion, List<Carril>> direccionCarriles = interseccionEntry.getValue();

            for (Map.Entry<Direccion, List<Carril>> entry : direccionCarriles.entrySet()) {
                Direccion direccion = entry.getKey();
                List<Carril> carriles = entry.getValue();

                for (Carril carril : carriles) {
                    List<Vehiculo> vehiculos = carril.getVehiculos();
                    Vehiculo vehiculoAnterior = null;

                    for (Vehiculo vehiculo : vehiculos) {
                        EstadoSemaforo estadoSemaforo = interseccion.getSemaforos().get(direccion).getEstado();
                        double distanciaAlSemaforo = distanciaAlSemaforoMasCercano(vehiculo);

                        if (estadoSemaforo == EstadoSemaforo.ROJO && distanciaAlSemaforo <= 80) {
                            vehiculo.setVelocidad(0);
                        } else if (vehiculoAnterior != null && distanciaEntre(vehiculo, vehiculoAnterior) < 40) {
                            vehiculo.setVelocidad(0);
                        } else {
                            vehiculo.setVelocidad(0.1);
                        }

                        if (carril.puedeRealizarAccion(vehiculo.getAccion())) {
                            aplicarAccionGiro(vehiculo, interseccion); // Aplicar la lógica de giro
                        } else {
                            vehiculo.setAccion(Accion.SEGUIR_RECTO); // Forzar a seguir recto si la acción no es permitida
                        }
                        vehiculo.mover();
                        dibujarVehiculo(vehiculo, intersecciones.indexOf(interseccion));
                        vehiculoAnterior = vehiculo;
                    }
                }
            }
        }
    }





    private void moverYdibujarVehiculosInferiores() {
        for (Map.Entry<Interseccion, Map<Direccion, List<Carril>>> interseccionEntry : carrilesPorInterseccion.entrySet()) {
            Interseccion interseccion = interseccionEntry.getKey();
            Map<Direccion, List<Carril>> direccionCarriles = interseccionEntry.getValue();

            for (Map.Entry<Direccion, List<Carril>> entry : direccionCarriles.entrySet()) {
                Direccion direccion = entry.getKey();
                List<Carril> carriles = entry.getValue();

                for (Carril carril : carriles) {
                    List<Vehiculo> vehiculos = carril.getVehiculosInferiores();
                    Vehiculo vehiculoAnterior = null;

                    for (Vehiculo vehiculo : vehiculos) {
                        EstadoSemaforo estadoSemaforo = interseccion.getSemaforos().get(direccion).getEstado();
                        double distanciaAlSemaforo = distanciaAlSemaforoMasCercanoInferior(vehiculo);

                        if (estadoSemaforo == EstadoSemaforo.ROJO && distanciaAlSemaforo <= 80) {
                            System.out.println("Entro1");
                            vehiculo.setVelocidad(0);
                        } else if (vehiculoAnterior != null && distanciaEntreInferiores(vehiculo, vehiculoAnterior) < 40) {
                            System.out.println("Entro2");
                            vehiculo.setVelocidad(0);
                        } else {
                            System.out.println("Entro3");
                            vehiculo.setVelocidad(0.1); // velocidad normal
                        }

                        vehiculo.mover2();
                        dibujarVehiculoInferiores(vehiculo, intersecciones.indexOf(interseccion));
                        vehiculoAnterior = vehiculo;
                    }
                }
            }
        }
    }

    private double distanciaAlSemaforoMasCercanoInferior(Vehiculo vehiculo) {
        double distanciaMinima = Double.MAX_VALUE;
        for (Interseccion interseccion : intersecciones) {
            double distancia = calcularDistancia(vehiculo.getPosXX(), vehiculo.getPosYY(),
                    interseccion.getPosX(), interseccion.getPosY());
            if (distancia < distanciaMinima) {
                distanciaMinima = distancia;
            }
        }
        return distanciaMinima;
    }

    private double distanciaEntreInferiores(Vehiculo v1, Vehiculo v2) {
        return Math.sqrt(Math.pow(v1.getPosXX() - v2.getPosXX(), 2) + Math.pow(v1.getPosYY() - v2.getPosYY(), 2));
    }

    private double distanciaAlSemaforoMasCercano(Vehiculo vehiculo) {
        double distanciaMinima = Double.MAX_VALUE;
        for (Interseccion interseccion : intersecciones) {
            double distancia = calcularDistancia(vehiculo.getPosX(), vehiculo.getPosY(),
                    interseccion.getPosX(), interseccion.getPosY());
            if (distancia < distanciaMinima) {
                distanciaMinima = distancia;
            }
        }
        return distanciaMinima;
    }

    private double calcularDistancia(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private double distanciaHastaSemaforo(Vehiculo vehiculo, Interseccion interseccion) {
        // Implementa la lógica para calcular la distancia del vehículo al semáforo
        // Esto dependerá de cómo se representan las posiciones en el canvas y las intersecciones
        return 0;
    }


    private boolean haySalidoDelCarril(Vehiculo vehiculo) {
        // Implementa la lógica para determinar si el vehículo ha salido del carril
        // Esto dependerá de cómo hayas definido los límites de los carriles
        return false;
    }
    private boolean hayVehiculoEmergenciaDetras(Vehiculo vehiculo, List<Vehiculo> vehiculos) {
        int index = vehiculos.indexOf(vehiculo);
        for (int i = index + 1; i < vehiculos.size(); i++) {
            if (vehiculos.get(i).getTipo() == TipoVehiculo.EMERGENCIA) {
                return true;
            }
        }
        return false;
    }

    private void procesarVehiculoEmergencia(Vehiculo vehiculo, Vehiculo vehiculoAnterior) {
        if (vehiculoAnterior == null || distanciaEntre(vehiculo, vehiculoAnterior) > 30) {
            vehiculo.setVelocidad(0.2);
        } else {
            vehiculo.setVelocidad(0);
        }
    }

    private void procesarVehiculoNormal(Vehiculo vehiculo, EstadoSemaforo estadoSemaforo, boolean hayEmergenciaDetras, Vehiculo vehiculoAnterior) {
        if (estadoSemaforo == ROJO && !estaEnInterseccion(vehiculo)) {
            vehiculo.setVelocidad(0);
        } else if (vehiculoAnterior != null && distanciaEntre(vehiculo, vehiculoAnterior) <= 30) {
            vehiculo.setVelocidad(0);
        } else if (hayEmergenciaDetras) {
            vehiculo.setVelocidad(0.15);
        } else {
            vehiculo.setVelocidad(0.1);
        }
    }

    private boolean estaEnInterseccion(Vehiculo vehiculo) {
        // Implementa la lógica para determinar si el vehículo está en la intersección
        // Esto dependerá de cómo hayas definido las coordenadas de las intersecciones
        return false;
    }

    private void aplicarAccion(Interseccion vehiculo) {
        // Implementa la lógica para aplicar la acción del vehículo (seguir recto, girar, etc.)
    }

    private double distanciaEntre(Vehiculo v1, Vehiculo v2) {
        return Math.sqrt(Math.pow(v1.getPosX() - v2.getPosX(), 2) + Math.pow(v1.getPosY() - v2.getPosY(), 2));
    }


    private void posicionarVehiculoEnCarril(Vehiculo vehiculo, Carril carril, int interseccionIndex) {
        double posX = 0; // Empieza desde el lado izquierdo
        double posY = 0;
        boolean esCalleSuperior = interseccionIndex >= 3;

        switch (vehiculo.getTipoCarril()) {
            case CENTRO:
                posY = esCalleSuperior ? 440 : 145;
                break;
            case IZQUIERDA:
                posY = esCalleSuperior ? 200 : 108;
                posX = esCalleSuperior ? 440 : 0;
                break;
            case DERECHA:
//                posY = esCalleSuperior ? 440 : 145;
                posY = esCalleSuperior ? 50 : 180;
                posX = esCalleSuperior ? 300 : 0;

                break;
        }

        vehiculo.setPosX(posX);
        vehiculo.setPosY(posY);
    }


    private void posicionarVehiculoEnCarrilInferior(Vehiculo vehiculo, Carril carril, int interseccionIndex) {
        double posXX = 0; // Empieza desde el lado derecho
        double posYY = 0;
        boolean esCalleInferior = interseccionIndex >= 3;

        switch (vehiculo.getTipoCarril()) {
            case CENTRO:
                posXX = esCalleInferior ? 1180 : 300;
                // Ajustar posX si es necesario, por ejemplo:
                posYY = esCalleInferior ? 145 : 100;
                break;
            case IZQUIERDA:
                posYY = esCalleInferior ? 180 : 1180;
                posXX = esCalleInferior ? 1180 : 145;
                break;
            case DERECHA:
                posXX = esCalleInferior ? 1180 : 741;
                posYY = esCalleInferior ? 110 : 78;
                break;
        }

        vehiculo.setPosXX(posXX);
        vehiculo.setPosYY(posYY);
    }



    public Canvas getCanvas() {
        return canvas;
    }

    public void agregarVehiculoEscenario2(TipoVehiculo tipoVehiculo, Accion accion, int interseccionIndex, TipoCarril tipoCarril, DoblarDonde doblarDonde, Direccion direccion) {
        Interseccion interseccion = intersecciones.get(interseccionIndex);
        List<Carril> carriles = carrilesPorInterseccion.get(interseccion).get(direccion);
        if (carriles != null) {
            Carril carrilAdecuado = seleccionarCarrilPorTipo(carriles, tipoCarril);
            if (carrilAdecuado != null) {
                Vehiculo vehiculo = new Vehiculo(UUID.randomUUID().toString(), tipoVehiculo, EstadoVehiculo.ESPERANDO, 0, 0, 0.1, accion, tipoCarril, doblarDonde, direccion);
                posicionarVehiculoEnCarril(vehiculo, carrilAdecuado, interseccionIndex);
                carrilAdecuado.agregarVehiculo(vehiculo);
            }
        } else {
            System.out.println("No se encontraron carriles para la dirección especificada.");
        }
    }

    public void agregarVehiculoEscenario2Inferior(TipoVehiculo tipoVehiculo, Accion accion, int interseccionIndex, TipoCarril tipoCarril, DoblarDonde doblarDonde, Direccion direccion) {
        Interseccion interseccion = intersecciones.get(interseccionIndex);
        List<Carril> carriles = carrilesPorInterseccion.get(interseccion).get(direccion);
        if (carriles != null) {
            Carril carrilAdecuado = seleccionarCarrilPorTipo(carriles, tipoCarril);
            if (carrilAdecuado != null) {
                Vehiculo vehiculo = new Vehiculo(UUID.randomUUID().toString(), tipoVehiculo, EstadoVehiculo.ESPERANDO, 0, 0, 0.1, accion, tipoCarril, doblarDonde, direccion);
                posicionarVehiculoEnCarrilInferior(vehiculo, carrilAdecuado, interseccionIndex);
                carrilAdecuado.agregarVehiculoInferiores(vehiculo);
            }
        } else {
            System.out.println("No se encontraron carriles para la dirección especificada.");
        }
    }

    private Carril seleccionarCarrilPorTipo(List<Carril> carriles, TipoCarril tipoCarril) {
        for (Carril carril : carriles) {
            if (carril.getTipo().equals(tipoCarril)) {
                return carril;
            }
        }
        return null;
    }

    private void aplicarAccionGiro(Vehiculo vehiculo, Interseccion interseccion) {
        if (vehiculo.estaEnInterseccion(interseccion.getPosX(), interseccion.getPosY())) {
            if (!vehiculo.isAccionAplicada()) {
                switch (vehiculo.getAccion()) {
                    case DOBLAR_DERECHA:
                        girarDerecha(vehiculo);
                        break;
                    case DOBLAR_IZQUIERDA:
                        girarIzquierda(vehiculo);
                        break;
                    default:
                        break;
                }
                vehiculo.setAccionAplicada(true); // Marca la acción como aplicada
            }
        } else {
            vehiculo.setAccionAplicada(false); // Resetea la acción cuando sale de la intersección
        }
    }



    private void girarDerecha(Vehiculo vehiculo) {
        switch (vehiculo.getDireccion()) {
            case DERECHA:
                vehiculo.setDireccion(Direccion.ABAJO);
                vehiculo.setPosX(vehiculo.getPosX() + 20);  // Ajusta según sea necesario
                vehiculo.setPosY(vehiculo.getPosY() + 20);  // Ajusta según sea necesario
                break;
            case IZQUIERDA:
                vehiculo.setDireccion(Direccion.ARRIBA);
                vehiculo.setPosX(vehiculo.getPosX() - 20);  // Ajusta según sea necesario
                vehiculo.setPosY(vehiculo.getPosY() - 20);  // Ajusta según sea necesario
                break;
            case ARRIBA:
                vehiculo.setDireccion(Direccion.DERECHA);
                vehiculo.setPosX(vehiculo.getPosX() + 20);  // Ajusta según sea necesario
                vehiculo.setPosY(vehiculo.getPosY() - 20);  // Ajusta según sea necesario
                break;
            case ABAJO:
                vehiculo.setDireccion(Direccion.IZQUIERDA);
                vehiculo.setPosX(vehiculo.getPosX() - 20);  // Ajusta según sea necesario
                vehiculo.setPosY(vehiculo.getPosY() + 20);  // Ajusta según sea necesario
                break;
        }
    }

    private void girarIzquierda(Vehiculo vehiculo) {
        switch (vehiculo.getDireccion()) {
            case DERECHA:
                vehiculo.setDireccion(Direccion.ARRIBA);
                vehiculo.setPosX(vehiculo.getPosX() + 20);  // Ajusta según sea necesario
                vehiculo.setPosY(vehiculo.getPosY() - 20);  // Ajusta según sea necesario
                break;
            case IZQUIERDA:
                vehiculo.setDireccion(Direccion.ABAJO);
                vehiculo.setPosX(vehiculo.getPosX() - 20);  // Ajusta según sea necesario
                vehiculo.setPosY(vehiculo.getPosY() + 20);  // Ajusta según sea necesario
                break;
            case ARRIBA:
                vehiculo.setDireccion(Direccion.IZQUIERDA);
                vehiculo.setPosX(vehiculo.getPosX() - 20);  // Ajusta según sea necesario
                vehiculo.setPosY(vehiculo.getPosY() - 20);  // Ajusta según sea necesario
                break;
            case ABAJO:
                vehiculo.setDireccion(Direccion.DERECHA);
                vehiculo.setPosX(vehiculo.getPosX() + 20);  // Ajusta según sea necesario
                vehiculo.setPosY(vehiculo.getPosY() + 20);  // Ajusta según sea necesario
                break;
        }
    }


}
