package es.uclm.GestiBiblioteca.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uclm.GestiBiblioteca.business.entities.Titulo;

@Repository
public interface TituloDAO extends JpaRepository<Titulo, Long> {
	
	List<Titulo> findByIsbn(String isbn);
	
}