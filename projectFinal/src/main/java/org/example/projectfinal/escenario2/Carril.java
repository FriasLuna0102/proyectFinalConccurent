package org.example.projectfinal.escenario2;

import org.example.projectfinal.enumeraciones.Accion;
import org.example.projectfinal.enumeraciones.TipoCarril;
import org.example.projectfinal.modelo.Vehiculo;

import java.util.ArrayList;
import java.util.List;

public class Carril {
    private TipoCarril tipo;
    private List<Vehiculo> vehiculos;

    public Carril(TipoCarril tipo) {
        this.tipo = tipo;
        this.vehiculos = new ArrayList<>();
    }

    public boolean puedeRealizarAccion(Accion accion) {
        switch (tipo) {
            case CENTRO:
                return accion == Accion.SEGUIR_RECTO;
            case DERECHA:
                return accion == Accion.SEGUIR_RECTO || accion == Accion.DOBLAR_DERECHA;
            case IZQUIERDA:
                return accion == Accion.DOBLAR_IZQUIERDA;
            default:
                return false;
        }
    }

    public boolean tieneEspacioDisponible() {
        // Implementa la lógica para verificar si hay espacio para un nuevo vehículo
        // Por ejemplo, podrías tener un límite máximo de vehículos por carril
        return vehiculos.size() < 5; // Ejemplo: máximo 5 vehículos por carril
    }


    public void agregarVehiculo(Vehiculo vehiculo) {
        vehiculos.add(vehiculo);
    }

    public List<Vehiculo> getVehiculos() {
        return vehiculos;
    }
}
