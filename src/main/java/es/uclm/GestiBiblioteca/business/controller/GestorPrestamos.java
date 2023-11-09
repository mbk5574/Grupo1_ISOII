package es.uclm.GestiBiblioteca.business.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.uclm.GestiBiblioteca.business.entities.Prestamo;
import es.uclm.GestiBiblioteca.business.entities.Titulo;
import es.uclm.GestiBiblioteca.business.entities.Usuario;
import es.uclm.GestiBiblioteca.persistence.PrestamoDAO;
import es.uclm.GestiBiblioteca.persistence.ReservaDAO;
import es.uclm.GestiBiblioteca.persistence.TituloDAO;
import es.uclm.GestiBiblioteca.persistence.UsuarioDAO;

@Controller
public class GestorPrestamos {

	@Autowired
	PrestamoDAO prestamoDAO;
	@Autowired
	ReservaDAO reservaDAO;
	@Autowired
	UsuarioDAO usuarioDAO;
	@Autowired
	TituloDAO tituloDAO;
	private static final Logger log = (Logger) LoggerFactory.getLogger(GestorPrestamos.class);

	@GetMapping("/altaPrestamos")
	public String mostrarFormularioPrestamo(Model model) {
		// Cargar la lista de usuarios y títulos disponibles desde la base de datos
		List<Usuario> usuarios = usuarioDAO.findAll();
		List<Titulo> titulos = tituloDAO.findAll();

		model.addAttribute("usuarios", usuarios);
		model.addAttribute("titulos", titulos);

		return "altaPrestamos";
	}

	@PostMapping("/realizar-prestamo")
	public String realizarPrestamo(@ModelAttribute Prestamo prestamo, Model model,
			RedirectAttributes redirectAttributes) {
		// Realiza la lógica de realizar un préstamo y almacénalo en la base de datos
		prestamoDAO.save(prestamo);
		log.info("Saved prestamo: " + prestamo);
		model.addAttribute("prestamoRealizado", true);
		model.addAttribute("mensaje", "Préstamo realizado exitosamente.");

		return "/resultPrestamo";
	}

	/**
	 * 
	 * @param isbn
	 * @param idEjemplar
	 * @param idUsuario
	 */
	public void realizarDevolucion(String isbn, String idEjemplar, String idUsuario) {
		// TODO - implement GestorPrestamos.realizarDevolucion
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param idUsuario
	 * @param isbn
	 */
	public void realizarReserva(String idUsuario, String isbn) {
		// TODO - implement GestorPrestamos.realizarReserva
		throw new UnsupportedOperationException();
	}

}