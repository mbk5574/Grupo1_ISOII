package es.uclm.GestiBiblioteca.business.entities;

import java.util.ArrayList;
import java.util.Collection;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Ejemplar {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private boolean disponible; // Campo agregado para disponibilidad

	@ManyToOne()
	@JoinColumn(name = "titulo_id", referencedColumnName = "id")
	private Titulo titulo;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Collection<Prestamo> prestamos = new ArrayList<>();
	

	

	public Ejemplar() {
	}

	public Ejemplar(Titulo titulo, boolean disponible) {
		super();
		this.titulo = titulo;
		this.disponible = disponible; // Inicializar el estado de disponibilidad
	}
	
	

	@Override
	public String toString() {
		return "Ejemplar{" + "id=" + id + ", tituloId=" + titulo.getId() + ", disponible=" + disponible + ", prestamos =" + prestamos +'}';
	}

	public Titulo getTitulo() {
		return titulo;
	}

	public void setTitulo(Titulo titulo) {
		this.titulo = titulo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isDisponible() {
		return disponible;
	}

	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}

	public Collection<Prestamo> getPrestamos() {
		return prestamos;
	}

	public void setPrestamos(Collection<Prestamo> prestamos) {
		this.prestamos = prestamos;
	}


	
}