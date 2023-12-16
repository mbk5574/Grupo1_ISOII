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
	public boolean eliminarTituloConVerificaciones(Long tituloId) {
	    Optional<Titulo> tituloOpt = tituloDAO.findById(tituloId);
	    if (tituloOpt.isPresent()) {
	        Titulo titulo = tituloOpt.get();

	        // Verificar si todos los ejemplares están disponibles para eliminación
	        for (Ejemplar ejemplar : titulo.getEjemplares()) {
	            boolean asociadoConPrestamoActivo = prestamoDAO.existsByEjemplarAndActivo(ejemplar, true);
	            boolean asociadoConReserva = reservaDAO.existsByEjemplar(ejemplar);
	            if (asociadoConPrestamoActivo || asociadoConReserva) {
	                return false; // No eliminar si hay ejemplares activos
	            }
	        }

	        // Eliminar todos los ejemplares asociados al título
	        for (Ejemplar ejemplar : titulo.getEjemplares()) {
	            reservaDAO.deleteByEjemplar(ejemplar); // Eliminar reservas asociadas
	            prestamoDAO.deleteByEjemplarAndActivo(ejemplar, false); // Eliminar préstamos inactivos
	            ejemplarDAO.delete(ejemplar); // Eliminar el ejemplar
	        }

	        // Eliminar referencias cruzadas en la relación many-to-many con autores
	        for (Autor autor : titulo.getAutores()) {
	            autor.getTitulos().remove(titulo);
	            if (autor.getTitulos().isEmpty()) {
	                autorDAO.delete(autor); // Eliminar el autor si ya no tiene títulos asociados
	            } else {
	                autorDAO.save(autor); // Actualizar el autor en la base de datos
	            }
	        }
	        titulo.setAutores(null); // Limpiar la lista de autores del título

	        // Eliminar el título
	        if (titulo instanceof Libro) {
	            libroDAO.delete((Libro) titulo);
	        } else if (titulo instanceof Revista) {
	            revistaDAO.delete((Revista) titulo);
	        } else {
	            tituloDAO.delete(titulo);
	        }

	        return true;
	    }
	    return false;
	}


	
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
	@Transactional
	public  void obtenerAutores(String[]autoresNombres,Collection<Autor>autores){
		for (String nombreAutor : autoresNombres) {
			String nombre = nombreAutor.trim();

			Optional<Autor> autorOpt = autorDAO.findByNombre(nombre);
			Autor autor;

			if (autorOpt.isPresent()) {

				autor = autorOpt.get();
			} else {

				autor = new Autor(nombre, "Apellido", null);
				autor = autorDAO.save(autor);
			}
			autores.add(autor);
		}
	}
	@Transactional
	public  Titulo guardarTitulo(Collection<Autor> autores,String tipoTitulo, Titulo titulo) {
		Titulo savedTitulo;
		if ("libro".equalsIgnoreCase(tipoTitulo)) {
				Libro nuevoLibro = new Libro(titulo.getTitulo_obra(),titulo.getIsbn(),autores,titulo.getEjemplares(),titulo.getPrestamos(),titulo.getReservas());
				savedTitulo = libroDAO.save(nuevoLibro);

			} else if ("revista".equalsIgnoreCase(tipoTitulo)) {
				Revista nuevaRevista = new Revista(titulo.getTitulo_obra(),titulo.getIsbn(),autores,titulo.getEjemplares(),titulo.getPrestamos(),titulo.getReservas());
				savedTitulo = revistaDAO.save(nuevaRevista);
			} else {
				
				return null;

			}
		return savedTitulo;
	}
}
