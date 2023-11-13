package es.uclm.GestiBiblioteca.persistence;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uclm.GestiBiblioteca.business.entities.Revista;

@Repository
public interface RevistaDAO extends JpaRepository<Revista, Long> {

	
}