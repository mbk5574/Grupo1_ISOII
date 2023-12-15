package es.uclm.GestiBiblioteca.services;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uclm.GestiBiblioteca.business.entities.*;
import es.uclm.GestiBiblioteca.persistence.AutorDAO;
import es.uclm.GestiBiblioteca.persistence.EjemplarDAO;
import es.uclm.GestiBiblioteca.persistence.LibroDAO;
import es.uclm.GestiBiblioteca.persistence.PrestamoDAO;
import es.uclm.GestiBiblioteca.persistence.ReservaDAO;
import es.uclm.GestiBiblioteca.persistence.RevistaDAO;
import es.uclm.GestiBiblioteca.persistence.TituloDAO;
import jakarta.transaction.Transactional;

@Service
public class TituloService {

	@Autowired
	private TituloDAO tituloDAO;

	@Autowired
	private AutorDAO autorDAO;

	@Autowired
	private LibroDAO libroDAO;

	@Autowired
	private RevistaDAO revistaDAO;
	@Autowired
	private EjemplarDAO ejemplarDAO;
	@Autowired
	private PrestamoDAO prestamoDAO;
	@Autowired
	private ReservaDAO reservaDAO;

	
	
	
	@Transactional
	public boolean eliminarTituloYAutores(Titulo titulo) {
		if (titulo == null)
			return false;

		Collection<Autor> autores = new HashSet<>(titulo.getAutores());

		for (Autor autor : autores) {
			autor.getTitulos().remove(titulo);
			if (autor.getTitulos().isEmpty()) {
				autorDAO.delete(autor);
			} else {
				autorDAO.save(autor);
			}
		}
		titulo.setAutores(null);

		if (titulo instanceof Libro) {
			libroDAO.delete((Libro) titulo);
		} else if (titulo instanceof Revista) {
			revistaDAO.delete((Revista) titulo);
		} else {
			tituloDAO.delete(titulo);
		}

		return true;
	}

	@Transactional
	public boolean eliminarEjemplarConVerificaciones(Long ejemplarId) {
		
		Optional<Ejemplar> ejemplarOpt = ejemplarDAO.findById(ejemplarId);
		if (ejemplarOpt.isPresent() && ejemplarOpt.get().isDisponible()) {
			Ejemplar ejemplar = ejemplarOpt.get();

			boolean asociadoConPrestamoActivo = prestamoDAO.existsByEjemplarAndActivo(ejemplar, true);
			boolean asociadoConReserva = reservaDAO.existsByEjemplar(ejemplar);

			if (!asociadoConPrestamoActivo && !asociadoConReserva) {
				reservaDAO.deleteByEjemplar(ejemplar);
				prestamoDAO.deleteByEjemplarAndActivo(ejemplar, false);
				ejemplarDAO.delete(ejemplar);
				return true;
			}
		}
		return false;
	}

}
