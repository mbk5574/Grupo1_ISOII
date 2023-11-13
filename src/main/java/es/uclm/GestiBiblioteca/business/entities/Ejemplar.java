package es.uclm.GestiBiblioteca.business.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Ejemplar {
	
	public Ejemplar() {	
	}
	
	public Ejemplar(Titulo titulo) {
		super();
		this.titulo=titulo;
		
	}
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
    
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "titulo_id", referencedColumnName = "id")
	private Titulo titulo;
	
	@Override
	public String toString() {
	    return "Ejemplar{" +
	           "id=" + id +
	           // Solo imprimir el ID del Titulo, no todo el objeto Titulo
	           ", tituloId=" + titulo.getId() +
	           '}';
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

	
}