package es.uclm.GestiBiblioteca.persistence;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

import es.uclm.GestiBiblioteca.business.entities.Prestamo;

@Repository
public interface PrestamoDAO extends JpaRepository<Prestamo, Integer> {

	
    Optional<Prestamo> findById(Integer id);
    List<Prestamo> findByActivoTrue();
    int countByUsuarioIdAndActivoTrue(@Param("usuarioId") Long usuarioId);


	
}
