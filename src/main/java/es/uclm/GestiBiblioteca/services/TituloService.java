package es.uclm.GestiBiblioteca.services;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uclm.GestiBiblioteca.business.entities.*;
import es.uclm.GestiBiblioteca.persistence.AutorDAO;
import es.uclm.GestiBiblioteca.persistence.LibroDAO;
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

}
