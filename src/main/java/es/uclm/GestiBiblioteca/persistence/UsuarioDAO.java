package es.uclm.GestiBiblioteca.persistence;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uclm.GestiBiblioteca.business.entities.Usuario;

@Repository

public interface UsuarioDAO extends JpaRepository<Usuario, Long> {
	
}