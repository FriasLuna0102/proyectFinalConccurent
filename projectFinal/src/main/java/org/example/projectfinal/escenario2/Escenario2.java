package org.example.projectfinal.escenario2;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.example.projectfinal.enumeraciones.*;
import org.example.projectfinal.modelo.Interseccion;
import org.example.projectfinal.modelo.Vehiculo;

import java.util.*;

public class Escenario2 {
    private List<Interseccion> intersecciones;
    private Canvas canvas;
    private GraphicsContext gc;
    private Map<Interseccion, Map<Direccion, List<Carril>>> carrilesPorInterseccion;

    public Escenario2() {
        intersecciones = new ArrayList<>();
        for (int i = 0; i < 6; i++) {  // Aumentado a 6 intersecciones
            intersecciones.add(new Interseccion(String.valueOf(i + 1)));
        }
        canvas = new Canvas(1200, 800);  // Aumentado el alto del canvas
        gc = canvas.getGraphicsContext2D();
        inicializarCarriles();
    }

    public void controlarSemaforos() {
        for (Interseccion interseccion : intersecciones) {
            interseccion.controlarSemaforos();
        }
    }

    public boolean esPosicionValida(int interseccionIndex, Direccion direccion) {
        // Implementa la lógica para verificar si es válido agregar un vehículo
        // en la intersección y dirección dadas
        Interseccion interseccion = intersecciones.get(interseccionIndex);
        Map<Direccion, List<Carril>> carriles = carrilesPorInterseccion.get(interseccion);
        List<Carril> carribesDireccion = carriles.get(direccion);

        // Verifica si hay espacio en alguno de los carriles de la dirección dada
        for (Carril carril : carribesDireccion) {
            if (carril.tieneEspacioDisponible()) {
                return true;
            }
        }
        return false;
    }

    public void iniciar() {
        // No es necesario inicializar los semáforos aquí
        // ya que se hace en el constructor de Interseccion

        // Puedes añadir otra lógica de inicialización si es necesaria
        // Por ejemplo, configurar posiciones iniciales de vehículos, etc.

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
        gc.clearRect(0, 0, 1200, 400);
        dibujarCalles();
        dibujarIntersecciones();
        dibujarVehiculos();
    }

    private void dibujarCalles() {
        gc.setFill(Color.GRAY);
        gc.fillRect(0, 100, 1200, 100); // Calle horizontal superior
        gc.fillRect(0, 400, 1200, 100); // Calle horizontal inferior
        for (int i = 0; i < 3; i++) {
            gc.fillRect(200 + i * 400, 0, 100, 600); // Calles verticales
        }

        // Agregar líneas que dividen los carriles
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
            dibujarInterseccion(250 + i * 400, 150);  // Intersecciones superiores
            dibujarInterseccion(250 + i * 400, 450);  // Intersecciones inferiores
        }
    }

    private void dibujarInterseccion(double x, double y) {
        gc.setFill(Color.BLACK);
        gc.fillRect(x - 10, y - 15, 20, 30);
        gc.setFill(Color.RED);
        gc.fillOval(x - 5, y - 10, 10, 10);
    }


    private void dibujarVehiculos() {
        for (int i = 0; i < intersecciones.size(); i++) {
            Interseccion interseccion = intersecciones.get(i);
            for (Map.Entry<Direccion, List<Carril>> entry : carrilesPorInterseccion.get(interseccion).entrySet()) {
                for (Carril carril : entry.getValue()) {
                    for (Vehiculo vehiculo : carril.getVehiculos()) {
                        dibujarVehiculo(vehiculo, i);
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
            case IZQUIERDA:
                gc.fillOval(posX, posY, 20, 10);
                break;
            case ABAJO:
                gc.translate(posX + 10, posY + 5); // Trasladar al centro del vehículo
                gc.rotate(180);
                gc.fillOval(-10, -5, 20, 10);
                break;
            case ARRIBA:
                gc.translate(posX + 10, posY + 5); // Trasladar al centro del vehículo
                gc.rotate(-90);
                gc.fillOval(-10, -5, 20, 10);
                break;
        }

        gc.restore(); // Restaurar el estado original del contexto gráfico
    }

    public void agregarVehiculo(TipoVehiculo tipo, Direccion direccion, Accion accion, int interseccionIndex) {
        Interseccion interseccion = intersecciones.get(interseccionIndex);
        List<Carril> carriles = carrilesPorInterseccion.get(interseccion).get(direccion);
        Carril carrilAdecuado = seleccionarCarrilAdecuado(carriles, accion);
        if (carrilAdecuado != null) {
            Vehiculo vehiculo = new Vehiculo(UUID.randomUUID().toString(), tipo, direccion, EstadoVehiculo.ESPERANDO, 0, 0, 0.1, accion);
            posicionarVehiculoEnCarril(vehiculo, carrilAdecuado, interseccionIndex);
            carrilAdecuado.agregarVehiculo(vehiculo);
        }
    }

    private Carril seleccionarCarrilAdecuado(List<Carril> carriles, Accion accion) {
        for (Carril carril : carriles) {
            if (carril.puedeRealizarAccion(accion)) {
                return carril;
            }
        }
        return null;
    }

    private void posicionarVehiculoEnCarril(Vehiculo vehiculo, Carril carril, int interseccionIndex) {
        double posX = 0, posY = 0;
        boolean esCalleInferior = interseccionIndex >= 3;
        int columnIndex = interseccionIndex % 3;

        switch (vehiculo.getTipoCarril()) {
            case CENTRO:
                posX = 50 + columnIndex * 400;
                posY = esCalleInferior ? 440 : 145;
                break;
            case IZQUIERDA:
                posX = 50 + columnIndex * 400;
                posY = esCalleInferior ? 440 : 180;
                break;
            case DERECHA:
                posX = 50 + columnIndex * 400;
                posY = esCalleInferior ? 440 : 110;
                break;

        }

        vehiculo.setPosX(posX);
        vehiculo.setPosY(posY);
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void agregarVehiculoEscenario2(TipoVehiculo tipoVehiculo, Accion accion, int interseccionIndex, TipoCarril tipoCarril, DoblarDonde doblarDonde, Direccion direccion) {
        Interseccion interseccion = intersecciones.get(interseccionIndex);
        List<Carril> carriles = carrilesPorInterseccion.get(interseccion).get(direccion);
        if (carriles != null) {
            Carril carrilAdecuado = seleccionarCarrilAdecuado(carriles, accion);
            if (carrilAdecuado != null) {
                Vehiculo vehiculo = new Vehiculo(UUID.randomUUID().toString(), tipoVehiculo, EstadoVehiculo.ESPERANDO, 0, 0, 0.1, accion, tipoCarril, doblarDonde, direccion);
                posicionarVehiculoEnCarril(vehiculo, carrilAdecuado, interseccionIndex);
                carrilAdecuado.agregarVehiculo(vehiculo);
            }
        } else {
            System.out.println("No se encontraron carriles para la dirección especificada.");
        }
    }
}
