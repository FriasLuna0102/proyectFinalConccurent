package org.example.projectfinal.modelo;

import org.example.projectfinal.enumeraciones.Direccion;
import org.example.projectfinal.enumeraciones.EstadoVehiculo;
import org.example.projectfinal.enumeraciones.TipoVehiculo;

public class Vehiculo {
    private String id;                  // Identificador único del vehículo
    private TipoVehiculo tipo;          // Tipo de vehículo (normal o emergencia)
    private Direccion direccion;        // Dirección del vehículo (derecha, recto, izquierda, vuelta en U)
    private EstadoVehiculo estado;      // Estado del vehículo (esperando, en movimiento, detenido, etc.)


    public Vehiculo(String id, TipoVehiculo tipo, Direccion direccion, EstadoVehiculo estado) {
        this.id = id;
        this.tipo = tipo;
        this.direccion = direccion;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TipoVehiculo getTipo() {
        return tipo;
    }

    public void setTipo(TipoVehiculo tipo) {
        this.tipo = tipo;
    }

    public int getDireccion() {
        return direccion.ordinal();
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public EstadoVehiculo getEstado() {
        return estado;
    }

    public void setEstado(EstadoVehiculo estado) {
        this.estado = estado;
    }

    //Metodos para el manejo de la clase

    public void avanzar() {
        this.estado = EstadoVehiculo.EN_MOVIMIENTO;
        System.out.println("Vehiculo " + id + " avanzando.");
    }

    public void detener() {
        this.estado = EstadoVehiculo.DETENIDO;
        System.out.println("Vehiculo " + id + " detenido.");
    }

    public boolean esEmergencia() {
        return this.tipo == TipoVehiculo.EMERGENCIA;
    }
}
