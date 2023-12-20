package es.uclm.GestiBiblioteca.business.controller;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.qos.logback.classic.Logger;
import es.uclm.GestiBiblioteca.business.entities.Autor;
import es.uclm.GestiBiblioteca.business.entities.Ejemplar;

import es.uclm.GestiBiblioteca.business.entities.Titulo;
import es.uclm.GestiBiblioteca.persistence.AutorDAO;
import es.uclm.GestiBiblioteca.persistence.EjemplarDAO;
import es.uclm.GestiBiblioteca.persistence.LibroDAO;
import es.uclm.GestiBiblioteca.persistence.RevistaDAO;
import es.uclm.GestiBiblioteca.persistence.TituloDAO;
import es.uclm.GestiBiblioteca.services.TituloService;

@Controller
public class GestorTitulos {

	private static final Logger log = (Logger) LoggerFactory.getLogger(GestorTitulos.class);

	@Autowired
	private EjemplarDAO ejemplarDAO;
	@Autowired
	private AutorDAO autorDAO;
	@Autowired
	private TituloDAO tituloDAO;
	@Autowired
	private RevistaDAO revistaDAO;
	@Autowired
	private LibroDAO libroDAO;
	@Autowired
	private TituloService tituloService;

	
	@GetMapping("/admin")
	public String adminPanel() {
		return "adminPanel";
	}

	@GetMapping("/altaTitulo")
	public String altaTituloForm(Model model) {
		model.addAttribute("titulo", new Titulo());
		log.info(tituloDAO.findAll().toString());
		List<Autor> autores = autorDAO.findAll();
		model.addAttribute("autores", autores);
		return "altaTitulo";
	}
	
	/**
	 * @param titulo
	 * @return
	 */
	@PostMapping("/altaTitulo")
	public String altaTituloSubmit(@ModelAttribute Titulo titulo,
			@RequestParam("tipoTitulo") String tipoTitulo,
			@RequestParam("autoresSeleccionados") List<String> autoresSeleccionados,
			RedirectAttributes redirectAttributes) {

		try {
			log.info("Iniciando altaTituloSubmit");

			String[] autoresNombres = autoresSeleccionados.toArray(new String[autoresSeleccionados.size()]);

			log.info("Autores ingresados: " + Arrays.toString(autoresNombres));
			log.info("Tipo de título seleccionado: " + tipoTitulo);
			log.info(
					"Datos del título: ISBN - " + titulo.getIsbn() + ", Título - " + titulo.getTitulo_obra());

			Collection<Autor> autores = new HashSet<>();
			tituloService.obtenerAutores(autoresNombres,autores);
			
			Titulo savedTitulo=tituloService.guardarTitulo(autores,tipoTitulo,titulo);
			if (savedTitulo == null) {
				log.error("Tipo de título no reconocido");
				return "Error";	
			}
			Ejemplar nuevoEjemplar = new Ejemplar(savedTitulo, true);
			ejemplarDAO.save(nuevoEjemplar);
			log.info("Ejemplar creado y asociado al título: " + savedTitulo.getTitulo_obra());

			log.info("Saved titulo: " + savedTitulo);
			redirectAttributes.addFlashAttribute("mensajeExito", "Título agregado exitosamente.");
			redirectAttributes.addFlashAttribute("titulo", savedTitulo);
			redirectAttributes.addFlashAttribute("tipoTitulo", tipoTitulo.equalsIgnoreCase("libro") ? "Libro" : "Revista");
			redirectAttributes.addFlashAttribute("autores",
					autores.stream().map(Autor::getNombre).collect(Collectors.toList()));
		} catch (Exception e) {

			redirectAttributes.addFlashAttribute("mensajeError", "Hubo un error al añadir el título.");
		}

		return "redirect:/resultAlta";
	}
	
	@GetMapping("/resultAlta")
	public String showResult(Model model) {
		return "resultadoAltaTitulo";
	}

	@GetMapping("/seleccionarIdTituloActualizar")
	public String mostrarFormularioSeleccion1() {
		return "seleccionarIdTituloActualizar";
	}

	@PostMapping("/seleccionarIdTituloActualizar")
	public String seleccionarIdTituloParaActualizar(@RequestParam Long tituloId,
			RedirectAttributes redirectAttributes) {
		redirectAttributes.addAttribute("tituloId", tituloId);
		return "redirect:/actualizarTitulo";
	}

	@GetMapping("/actualizarTitulo")
	public String actualizarTituloForm(@RequestParam Long tituloId, Model model) {
		Titulo existingTitulo = tituloDAO.findById(tituloId).orElse(null);

		if (existingTitulo == null) {
			return "redirect:/seleccionarIdTituloActualizar";
		}

		model.addAttribute("actualizarTitulo", existingTitulo);

		return "actualizarTitulo";
	}

	@PostMapping("/actualizarTitulo")
	public String actualizarTituloSubmit(@ModelAttribute Titulo titulo, Model model, RedirectAttributes redirectAttributes) {
		Titulo existingTitulo = tituloDAO.findById(titulo.getId()).orElse(null);
		String result2; 

		if (existingTitulo != null) {
			existingTitulo.setTitulo_obra(titulo.getTitulo_obra());
			existingTitulo.setIsbn(titulo.getIsbn());

			tituloDAO.save(existingTitulo);

			log.info("Updated titulo: " + existingTitulo);
			result2 = "Título actualizado exitosamente.";
			redirectAttributes.addFlashAttribute("result2", "Título actualizado exitosamente.");
	        redirectAttributes.addFlashAttribute("titulo", existingTitulo);

		} else {
			result2 = "No se encontró el título para actualizar.";
			log.warn(result2);
	        redirectAttributes.addFlashAttribute("result2", "No se encontró el título para actualizar.");
		}

		model.addAttribute("titulo", existingTitulo);
		model.addAttribute("result2", result2);

		return "redirect:/resultadoActualizacion";
	}

	@GetMapping("/resultadoActualizacion")
	public String mostrarResultadoActualizacion() {
		return "resultadoActualizacionTitulo";
	}

	@GetMapping("/borrarTitulo")
	public String mostrarFormularioBorrarTitulo(Model model) {
		model.addAttribute("borrarTitulo", new Titulo());
		return "borrarTitulo"; 
	}

	@PostMapping("/borrarTitulo")
	public String borrarTitulo(@RequestParam("isbn") String isbn, RedirectAttributes redirectAttributes) {
	    List<Titulo> titulosABorrar = tituloDAO.findByIsbn(isbn);

	    if (!titulosABorrar.isEmpty()) {
	        for (Titulo titulo : titulosABorrar) {
	            // Utiliza el método modificado para verificar y eliminar el título
	            boolean eliminado = tituloService.eliminarTituloConVerificaciones(titulo.getId());
	            if (eliminado) {
	                redirectAttributes.addFlashAttribute("mensajeExito", "Título con ISBN " + isbn + " borrado exitosamente");
	            } else {
	                // Este mensaje se muestra si el título no pudo ser eliminado debido a ejemplares activos
	                redirectAttributes.addFlashAttribute("mensajeError", "El título con ISBN " + isbn + " no puede ser borrado debido a ejemplares activos o reservados");
	                break; 
	            }
	        }
	    } else {
	        redirectAttributes.addFlashAttribute("mensajeError", "No se encontró ningún título con ISBN " + isbn);
	    }

	    return "redirect:/mostrarResultadoBorrado";
	}



	@GetMapping("/mostrarResultadoBorrado")
	public String mostrarResultadoBorrado(Model model) {
		return "resultadoBorrarTitulo"; 
	}

	@GetMapping("/altaEjemplar")
	public String mostrarAltaEjemplarForm(Model model) {
		List<Titulo> todosTitulos = tituloDAO.findAll();
		model.addAttribute("todosTitulos", todosTitulos); 
		model.addAttribute("altaEjemplar", new Ejemplar());
		return "altaEjemplar"; 
	}

	@PostMapping("/altaEjemplar")
	public String altaEjemplar(@RequestParam("id") Long id, @ModelAttribute Ejemplar ejemplar,
	        RedirectAttributes redirectAttributes) {
	    try {
	        Titulo titulo = tituloDAO.findById(id).orElseThrow(() -> new RuntimeException("Titulo no encontrado"));
	        Ejemplar nuevoEjemplar = new Ejemplar(titulo, true);
	        ejemplarDAO.save(nuevoEjemplar);
	        log.info("Nuevo ejemplar creado y asociado al título: " + titulo.getTitulo_obra());
	        redirectAttributes.addFlashAttribute("ejemplarAgregado", true);
	        redirectAttributes.addFlashAttribute("mensaje", "Nuevo ejemplar agregado exitosamente al título: " + titulo.getTitulo_obra());
	        redirectAttributes.addFlashAttribute("titulo", titulo);
	    } catch (RuntimeException e) {
	        log.error("El título no existe.", e);
	        redirectAttributes.addFlashAttribute("ejemplarAgregado", false);
	        redirectAttributes.addFlashAttribute("error", "El título con ID " + id + " no existe.");
	    }
	    return "redirect:/mostrarResultadoAltaEjemplar";
	}

	
	@GetMapping("/mostrarResultadoAltaEjemplar")
	public String mostrarAltaEjemplar(Model model) {
		return "resultadoAltaEjemplar"; 
	}

	@GetMapping("/bajaEjemplar")
	public String mostrarBajaEjemplarForm(Model model) {
		List<Ejemplar> todosEjemplares = ejemplarDAO.findAll(); 
		model.addAttribute("todosEjemplares", todosEjemplares); 
		return "bajaEjemplar"; 
	}


	@PostMapping("/bajaEjemplar")
	public String bajaEjemplar(@RequestParam("idsEjemplares") List<Long> idsEjemplares, RedirectAttributes redirectAttributes) {
	    boolean exito = true;
		if(idsEjemplares.isEmpty())
		{
			redirectAttributes.addFlashAttribute("error", "No se seleccionó ningún ejemplar.");
			return "redirect:/resultadoBajaEjemplar";
		}	
	    for (Long id : idsEjemplares) {
	        boolean resultado = tituloService.eliminarEjemplarConVerificaciones(id);
	        if (!resultado) {
	            String mensajeError = "El ejemplar con id: " + id + " no puede ser eliminado.";
	            log.info(mensajeError);
	            redirectAttributes.addFlashAttribute("error", mensajeError);
	            exito = false;
	        }
	    }

	    if (exito) {
	        redirectAttributes.addFlashAttribute("mensaje", "Ejemplares dados de baja exitosamente.");
	    }

	    return "redirect:/resultadoBajaEjemplar";
	}

	@GetMapping("/resultadoBajaEjemplar")
	public String mostrarResultadoBaja() {
		return "resultadoBajaEjemplar";
	}

}