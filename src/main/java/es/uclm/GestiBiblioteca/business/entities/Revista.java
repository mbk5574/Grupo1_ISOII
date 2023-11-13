package es.uclm.GestiBiblioteca.business.entities;

import java.util.Collection;
import java.util.Set;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("revista")
public class Revista extends Titulo {

	public Revista() {
		super();
	}

	// Constructor que inicializa atributos de Titulo
	public Revista(String titulo, String isbn, Set<Autor> autores) {
		super(autores, null, null, null, titulo, isbn);
	}

}
