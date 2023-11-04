package es.uclm.GestiBiblioteca.business.controller;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.slf4j.LoggerFactory.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

	@GetMapping("/seleccionarTituloParaActualizar")
	public String mostrarFormularioSeleccion() {
	    return "seleccionarTitulo"; // reemplaza esto con el nombre del archivo .html donde colocaste el formulario.
	}
	
	@PostMapping("/seleccionarTituloParaActualizar")
	public String seleccionarTituloParaActualizar(@RequestParam Long tituloId, RedirectAttributes redirectAttributes) {
	    redirectAttributes.addAttribute("tituloId", tituloId);
	    return "redirect:/actualizarTitulo";
	}
	
	
	@GetMapping("/actualizarTitulo")
	public String actualizarTituloForm(@RequestParam Long tituloId, Model model) {
	    Titulo existingTitulo = tituloDAO.findById(tituloId).orElse(null);
	    
	    if(existingTitulo == null) {
	        // Aquí puedes manejar el error. Por ahora, solo redirige al usuario de vuelta al formulario de selección.
	        return "redirect:/seleccionarTituloParaActualizar";
	    }
	    
	    model.addAttribute("altaTitulos", existingTitulo);
	    
	    return "altaTitulos"; // Nombre de la vista que muestra el formulario de actualización
	}
	
	
	@PostMapping("/actualizarTitulo")
	public String actualizarTituloSubmit(@ModelAttribute Titulo titulo, Model model) {
	    Titulo existingTitulo = tituloDAO.findById(titulo.getId()).orElse(null);
	    String result2; // Variable para guardar mensajes resultantes
	    
	    if (existingTitulo != null) {
	        existingTitulo.setTitulo_obra(titulo.getTitulo_obra());
	        existingTitulo.setIsbn(titulo.getIsbn());
	        existingTitulo.setNumReserva(titulo.getNumReserva());

	        tituloDAO.save(existingTitulo);
	        
	        log.info("Updated titulo: " + existingTitulo);
	        result2 = "Título actualizado exitosamente.";
	        
	    } else {
	        result2 = "No se encontró el título para actualizar.";
	        log.warn(result2);
	    }
	    
	    model.addAttribute("titulo", existingTitulo);
	    model.addAttribute("result2", result2); // Agregar el mensaje resultante al modelo
	    
	    return "result2"; 
	}

	
	@GetMapping("/borrarTitulo")
	public String mostrarFormularioBorrarTitulo(Model model) {
	    model.addAttribute("altaTitulos", new Titulo());
	    return "altaTitulos"; // Esto es el nombre de la vista (template) que contiene el formulario.
	}
	
	@PostMapping("/borrarTitulo")
	public String borrarTitulo(@RequestParam("isbn") String isbn, Model model) {
	    boolean borrado = true;
	    String result3;
	    List<Titulo> titulosABorrar = tituloDAO.findByIsbn(isbn);

	    if (!titulosABorrar.isEmpty()) {
	        tituloDAO.deleteAll(titulosABorrar);
	        log.info("Titulos con ISBN " + isbn + " borrados exitosamente");
	        result3 = "Titulos con ISBN " + isbn + " borrados exitosamente";
	        // Establece la variable borradoExitoso en true si el borrado es exitoso
	    } else {
	    	result3 = "No se encontro ningún titulo con ISBN " + isbn;
	    	log.info(result3);
	    	borrado = false;
	    	
	    }

	    model.addAttribute("result3", result3);
	    // Agrega la variable borradoExitoso al modelo para que Thymeleaf la utilice
	    model.addAttribute("borrado", borrado);

	    return "result3";
	}

	
	public void altaEjemplar(Titulo t) {
		// TODO - implement GestorTitulos.altaEjemplar
		throw new UnsupportedOperationException();
	}


	public void bajaEjemplar(Titulo t) {
		// TODO - implement GestorTitulos.bajaEjemplar
		throw new UnsupportedOperationException();
	}

}