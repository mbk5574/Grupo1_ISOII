package es.uclm.GestiBiblioteca.business.entities;

import java.util.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;



@Entity
public class Reserva {

	@ManyToOne
	@JoinColumn(name = "usuario_id", referencedColumnName = "id")
	Usuario usuario;
	
	@ManyToOne
    @JoinColumn(name = "ejemplar_id")
    private Ejemplar ejemplar;

	@ManyToOne
	@JoinColumn(name = "titulo_id")
	private Titulo titulo;

	@Column
	private Date fecha;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id_reserva;

	public Reserva() {

	}

	public Reserva(Usuario usuario, Ejemplar ejemplar, Titulo titulo, Date fecha) {
		super();
		this.fecha = fecha;
		this.usuario = usuario;
		this.ejemplar = ejemplar;
		this.titulo = titulo;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Titulo getTitulo() {
		return titulo;
	}

	public void setTitulo(Titulo titulo) {
		this.titulo = titulo;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public int getId_reserva() {
		return id_reserva;
	}

	public void setId_reserva(int id_reserva) {
		this.id_reserva = id_reserva;
	}
	

	public Ejemplar getEjemplar() {
		return ejemplar;
	}

	public void setEjemplar(Ejemplar ejemplar) {
		this.ejemplar = ejemplar;
	}

	@Override
	public String toString() {
		return "Reserva [usuario=" + usuario + ", titulo=" + titulo + ", fecha=" + fecha + ", id_reserva=" + id_reserva
				+ "]";
	}

}