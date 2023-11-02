package es.uclm.GestiBiblioteca.business.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("libro")
public class Libro extends Titulo {
	
	
    
}