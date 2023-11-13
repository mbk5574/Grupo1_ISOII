package es.uclm.GestiBiblioteca.business.entities;

import java.util.Collection;
import java.util.Set;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@DiscriminatorValue("libro")
public class Libro extends Titulo {
    
	public Libro() {
        super();
    }

    public Libro(String titulo, String isbn, Set<Autor> autores) {
        super(autores, null, null, null, titulo, isbn);
    }
	
	
}