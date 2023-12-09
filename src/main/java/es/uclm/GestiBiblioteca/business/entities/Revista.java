package es.uclm.GestiBiblioteca.business.entities;

import java.util.Set;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("revista")
public class Revista extends Titulo {

	public Revista() {
		super();
	}

	public Revista(String titulo, String isbn, Set<Autor> autores) {
		super(autores, null, null, null, titulo, isbn);
	}

}
