package es.uclm.GestiBiblioteca.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import es.uclm.GestiBiblioteca.business.entities.Autor;

@Repository
public interface AutorDAO extends JpaRepository<Autor, Long> {

    Optional<Autor> findByNombre(String nombre);

}