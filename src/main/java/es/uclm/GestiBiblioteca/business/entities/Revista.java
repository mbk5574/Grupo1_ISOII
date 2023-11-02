package es.uclm.GestiBiblioteca.business.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("revista")
public class Revista extends Titulo {

}
