package es.uclm.GestiBiblioteca.business.entities;



import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Usuario {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Collection<Prestamo> prestamos = new ArrayList<>();;

	@OneToMany
	private Collection<Reserva> reservas = new ArrayList<>();;


	@Column
	private String nombre;
	@Column
	private String apellidos;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")

	private Date fechaFinPenalizacion;

	public Usuario() {

	}

	public Usuario(Collection<Prestamo> prestamos, Collection<Reserva> reservas, String nombre, String apellidos, Date fechaFinPenalizacion) {
		this.prestamos = prestamos;
		this.reservas = reservas;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.fechaFinPenalizacion = fechaFinPenalizacion;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", prestamos=" + prestamos + ", reservas=" + reservas + ", nombre=" + nombre
				+ ", apellidos=" + apellidos + ", fechaFinPenalizacion=" + fechaFinPenalizacion + ", attribute=" + "]";
	}

}
