package es.uclm.GestiBiblioteca.persistence;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uclm.GestiBiblioteca.business.entities.Libro;

@Repository
public interface LibroDAO extends JpaRepository<Libro, Long> {

	
}