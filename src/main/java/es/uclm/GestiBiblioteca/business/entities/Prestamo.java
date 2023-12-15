package es.uclm.GestiBiblioteca.business.entities;

import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Prestamo {

	@ManyToOne
	@JoinColumn(name = "usuario_id", referencedColumnName = "id")
	Usuario usuario;

	@ManyToOne
	@JoinColumn(name = "ejemplar_id", referencedColumnName = "id")
	private Ejemplar ejemplar;

	@ManyToOne()
	@JoinColumn(name = "titulo_id", referencedColumnName = "id")
	private Titulo titulo;

	@Column
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fechaInicio;

	@Column
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fechaFin;

	@Column
	private Boolean activo;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id_prestamo;

	public Prestamo() {

	}

	public Prestamo(Usuario usuario, Titulo titulo, Date fechaInicio, Date fechaFin, Boolean activo, Ejemplar ejemplar) {
		this.usuario = usuario;
		this.titulo = titulo;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.activo = activo;
		this.ejemplar = ejemplar;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public Ejemplar getEjemplar() {
        return ejemplar;
    }

    public void setEjemplar(Ejemplar ejemplar) {
        this.ejemplar = ejemplar;
    }

	public Titulo getTitulo() {
		return titulo;
	}

	public void setTitulo(Titulo titulo) {
		this.titulo = titulo;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public int getId_prestamo() {
		return id_prestamo;
	}

	public void setId_prestamo(int id_prestamo) {
		this.id_prestamo = id_prestamo;
	}

	@Override
	public String toString() {
		return "Prestamo [usuario=" + usuario + ", titulo=" + titulo + ", fechaInicio=" + fechaInicio + ", fechaFin="
				+ fechaFin + ", activo=" + activo + ", id_prestamo=" + id_prestamo + ", ejemplar=" + ejemplar + "]";
	}

}