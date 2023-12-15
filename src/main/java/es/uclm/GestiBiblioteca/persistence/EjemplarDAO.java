package es.uclm.GestiBiblioteca.persistence;

import org.springframework.stereotype.Repository;
import es.uclm.GestiBiblioteca.business.entities.Ejemplar;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface EjemplarDAO extends JpaRepository<Ejemplar, Long>  {

    List<Ejemplar> findByDisponibleTrue();
    List<Ejemplar> findByDisponibleFalse();

	
}
