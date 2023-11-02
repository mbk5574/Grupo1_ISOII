package es.uclm.GestiBiblioteca.business.controller;

import org.slf4j.LoggerFactory;
import org.slf4j.LoggerFactory.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ch.qos.logback.classic.Logger;
import es.uclm.GestiBiblioteca.business.entities.Titulo;
import es.uclm.GestiBiblioteca.persistence.AutorDAO;
import es.uclm.GestiBiblioteca.persistence.EjemplarDAO;
import es.uclm.GestiBiblioteca.persistence.TituloDAO;


@Controller
public class GestorTitulos {
	
	private static final Logger log = (Logger) LoggerFactory.getLogger(GestorTitulos.class);

	
	@Autowired
	private EjemplarDAO ejemplarDAO;
	@Autowired
	private AutorDAO autorDAO;
	@Autowired
	private TituloDAO tituloDAO;
	

	@GetMapping("/altaTitulos")
	public String altaTituloForm( Model model) {
		model.addAttribute("altaTitulos", new Titulo());
		log.info(tituloDAO.findAll().toString());
		return "altaTitulos";
	}
	
	@PostMapping("/altaTitulos")
	public String altaTituloSubmit( @ModelAttribute Titulo altaTitulos, Model model) {
		model.addAttribute("altaTitulos", altaTitulos);
		Titulo savedTitulo = tituloDAO.save(altaTitulos);
		log.info("Saved titulo: " + savedTitulo);
		return "result";
	}

	/**
	 * 
	 * @param t
	 */
	public void actualizarTitulo(Titulo t) {
		// TODO - implement GestorTitulos.actualizarTitulo
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param t
	 */
	public void borrarTitulo(Titulo t) {
		// TODO - implement GestorTitulos.borrarTitulo
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param t
	 */
	public void altaEjemplar(Titulo t) {
		// TODO - implement GestorTitulos.altaEjemplar
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param t
	 */
	public void bajaEjemplar(Titulo t) {
		// TODO - implement GestorTitulos.bajaEjemplar
		throw new UnsupportedOperationException();
	}

}