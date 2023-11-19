package es.uclm.GestiBiblioteca.business.entities;

import java.util.*;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Collection;
import java.util.Date;

@Entity
public class Usuario {
	
	public Usuario() {
		
	}public Usuario(Collection<Prestamo> prestamos,Collection<Reserva> reservas,String nombre,String apellidos,Date fechaFinPenalizacion,int attribute){
		this.prestamos=prestamos;
		this.reservas=reservas;
		this.nombre=nombre;
		this.apellidos=apellidos;
		this.fechaFinPenalizacion=fechaFinPenalizacion;
		this.attribute=attribute;
	}
	
    @Id
    private String id;

    @OneToMany
    private Collection<Prestamo> prestamos;

    @OneToMany
    private Collection<Reserva> reservas;

    private String nombre;
    private String apellidos;

    @Temporal(TemporalType.DATE)
    private Date fechaFinPenalizacion;

    private int attribute;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Collection<Prestamo> getPrestamos() {
		return prestamos;
	}
	public void setPrestamos(Collection<Prestamo> prestamos) {
		this.prestamos = prestamos;
	}
	public Collection<Reserva> getReservas() {
		return reservas;
	}
	public void setReservas(Collection<Reserva> reservas) {
		this.reservas = reservas;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public Date getFechaFinPenalizacion() {
		return fechaFinPenalizacion;
	}
	public void setFechaFinPenalizacion(Date fechaFinPenalizacion) {
		this.fechaFinPenalizacion = fechaFinPenalizacion;
	}
	public int getAttribute() {
		return attribute;
	}
	public void setAttribute(int attribute) {
		this.attribute = attribute;
	}
	@Override
	public String toString() {
		return "Usuario [id=" + id + ", prestamos=" + prestamos + ", reservas=" + reservas + ", nombre=" + nombre
				+ ", apellidos=" + apellidos + ", fechaFinPenalizacion=" + fechaFinPenalizacion + ", attribute="
				+ attribute + "]";
	}

   
}
