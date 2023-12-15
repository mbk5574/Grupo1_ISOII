package es.uclm.GestiBiblioteca.persistence;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uclm.GestiBiblioteca.business.entities.Ejemplar;
import es.uclm.GestiBiblioteca.business.entities.Reserva;

@Repository
public interface ReservaDAO extends JpaRepository<Reserva, Integer> {

    List<Reserva> findByEjemplar(Ejemplar ejemplar);
    boolean existsByEjemplar(Ejemplar ejemplar);
    void deleteByEjemplar(Ejemplar ejemplar);


}
