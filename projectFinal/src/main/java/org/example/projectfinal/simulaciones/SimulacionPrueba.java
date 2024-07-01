package org.example.projectfinal.simulaciones;

import org.example.projectfinal.controladores.ControladorSimulacion;
import org.example.projectfinal.enumeraciones.Direccion;
import org.example.projectfinal.enumeraciones.EstadoVehiculo;
import org.example.projectfinal.enumeraciones.TipoVehiculo;
import org.example.projectfinal.modelo.Vehiculo;

public class SimulacionPrueba {
    public static void main(String[] args) {
        // Crear el controlador de simulación
        ControladorSimulacion controlador = new ControladorSimulacion();

        // Iniciar la simulación
        controlador.iniciarSimulacion();

        // Generar algunos vehículos en la intersección "1"
        Vehiculo vehiculo1 = new Vehiculo("V1", TipoVehiculo.NORMAL, Direccion.DERECHA, EstadoVehiculo.ESPERANDO);
        Vehiculo vehiculo2 = new Vehiculo("V2", TipoVehiculo.EMERGENCIA, Direccion.RECTO, EstadoVehiculo.ESPERANDO);
        Vehiculo vehiculo3 = new Vehiculo("V3", TipoVehiculo.NORMAL, Direccion.IZQUIERDA, EstadoVehiculo.ESPERANDO);

        controlador.generarVehiculo("1", vehiculo1);
        controlador.generarVehiculo("1", vehiculo2);
        controlador.generarVehiculo("1", vehiculo3);

        // Esperar un tiempo para observar los cambios
        try {
            Thread.sleep(60000); // Esperar 60 segundos
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
