package es.uclm.GestiBiblioteca.business.entities;

import java.util.Collection;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;


@Entity
@DiscriminatorValue("libro")
public class Libro extends Titulo {
    
    public Libro() {
        super();
    }

    public Libro(String titulo, String isbn, Collection<Autor> autores, Collection<Ejemplar> ejemplares,
            Collection<Prestamo> prestamos, Collection<Reserva> reservas) {
        super(autores,ejemplares,prestamos, reservas, titulo, isbn);
        
    }

   
	
	
}