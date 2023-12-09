package es.uclm.GestiBiblioteca.business.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

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

	public Ejemplar() {
	}

	public Ejemplar(Titulo titulo, boolean disponible) {
		super();
		this.titulo = titulo;
		this.disponible = disponible; // Inicializar el estado de disponibilidad
	}

	@Override
	public String toString() {
		return "Ejemplar{" + "id=" + id + ", tituloId=" + titulo.getId() + ", disponible=" + disponible + '}';
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

}