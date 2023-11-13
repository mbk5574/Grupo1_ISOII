package es.uclm.GestiBiblioteca.business.controller;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import es.uclm.GestiBiblioteca.business.entities.Libro;
import es.uclm.GestiBiblioteca.business.entities.Prestamo;
import es.uclm.GestiBiblioteca.business.entities.Reserva;
import es.uclm.GestiBiblioteca.business.entities.Revista;
import es.uclm.GestiBiblioteca.business.entities.Titulo;
import es.uclm.GestiBiblioteca.persistence.AutorDAO;
import es.uclm.GestiBiblioteca.persistence.EjemplarDAO;
import es.uclm.GestiBiblioteca.persistence.LibroDAO;
import es.uclm.GestiBiblioteca.persistence.PrestamoDAO;
import es.uclm.GestiBiblioteca.persistence.ReservaDAO;
import es.uclm.GestiBiblioteca.persistence.RevistaDAO;
import es.uclm.GestiBiblioteca.persistence.TituloDAO;
import jakarta.transaction.Transactional;

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

	@GetMapping("/altaTitulos")
	public String altaTituloForm(Model model) {
		model.addAttribute("altaTitulos", new Titulo());
		log.info(tituloDAO.findAll().toString());
		return "altaTitulos";
	}

	@PostMapping("/altaTitulos")
	public String altaTituloSubmit(@ModelAttribute Titulo altaTitulos, @RequestParam("autoresString") String autoresString, @RequestParam("tipoTitulo") String tipoTitulo, Model model) {
		log.info("Iniciando altaTituloSubmit");
	    // Dividir los autores ingresados en un array
	    String[] autoresNombres = autoresString.split(",");
	    
	    log.info("Autores ingresados: " + Arrays.toString(autoresNombres));
	    log.info("Tipo de título seleccionado: " + tipoTitulo);
	    log.info("Datos del título: ISBN - " + altaTitulos.getIsbn() + ", Título - " + altaTitulos.getTitulo_obra());
	    
	    // Preparar la lista de autores
	    Collection<Autor> autores= new HashSet<>();

	    // Para cada nombre de autor, crea o encuentra el autor y lo agrega a la lista de autores del título
	    for (String nombreAutor : autoresNombres) {
	        String nombre = nombreAutor.trim();

	        Optional<Autor> autorOpt = autorDAO.findByNombre(nombre);
	        Autor autor;

	        // Comprobar si el autor existe
	        if (autorOpt.isPresent()) {
	            // Si existe, usar ese autor
	            autor = autorOpt.get();
	        } else {
	            // Si no existe, crear un nuevo autor y guardarlo
	            autor = new Autor(nombre, "Apellido", null);
	            autor = autorDAO.save(autor);
	        }

	        autores.add(autor);
	    }

	    Titulo savedTitulo;
	    if ("libro".equalsIgnoreCase(tipoTitulo)) {
	    	
	        Libro nuevoLibro = new Libro();
	        nuevoLibro.setTitulo_obra(altaTitulos.getTitulo_obra());
	        nuevoLibro.setIsbn(altaTitulos.getIsbn());
	        nuevoLibro.setAutores(autores);
	        savedTitulo = libroDAO.save(nuevoLibro);
	        model.addAttribute("tipoTitulo", "Libro");
	        
	    } else if ("revista".equalsIgnoreCase(tipoTitulo)) {
	    	
	        Revista nuevaRevista = new Revista();
	        nuevaRevista.setTitulo_obra(altaTitulos.getTitulo_obra());
	        nuevaRevista.setIsbn(altaTitulos.getIsbn());
	        nuevaRevista.setAutores(autores);
	        savedTitulo = revistaDAO.save(nuevaRevista);
	        model.addAttribute("tipoTitulo", "Revista");
	        
	    } else {
	    	
	        log.error("Tipo de título no reconocido");
	        return "error"; 
	        
	    }
	    
	    Ejemplar nuevoEjemplar = new Ejemplar(savedTitulo);
	    ejemplarDAO.save(nuevoEjemplar);
	    log.info("Ejemplar creado y asociado al título: " + savedTitulo.getTitulo_obra());   

	    log.info("Saved titulo: " + savedTitulo);

	    model.addAttribute("altaTitulos", savedTitulo);
	    return "result";
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

		model.addAttribute("altaTitulos", existingTitulo);

		return "altaTitulos"; 
	}

	@PostMapping("/actualizarTitulo")
	public String actualizarTituloSubmit(@ModelAttribute Titulo titulo, Model model) {
		Titulo existingTitulo = tituloDAO.findById(titulo.getId()).orElse(null);
		String result2; // Variable para guardar mensajes resultantes

		if (existingTitulo != null) {
			existingTitulo.setTitulo_obra(titulo.getTitulo_obra());
			existingTitulo.setIsbn(titulo.getIsbn());

			tituloDAO.save(existingTitulo);

			log.info("Updated titulo: " + existingTitulo);
			result2 = "Título actualizado exitosamente.";

		} else {
			result2 = "No se encontró el título para actualizar.";
			log.warn(result2);
		}

		model.addAttribute("titulo", existingTitulo);
		model.addAttribute("result2", result2); // agegamos el mensaje resultante al modelo

		return "result2";
	}

	@GetMapping("/borrarTitulo")
	public String mostrarFormularioBorrarTitulo(Model model) {
		model.addAttribute("altaTitulos", new Titulo());
		return "altaTitulos"; // Esto es el nombre de la vista (template) que contiene el formulario.
	}

	@PostMapping("/borrarTitulo")
	@Transactional
	public String borrarTitulo(@RequestParam("isbn") String isbn, RedirectAttributes redirectAttributes) {
	    List<Titulo> titulosABorrar = tituloDAO.findByIsbn(isbn);

	    if (!titulosABorrar.isEmpty()) {
	        for (Titulo titulo : titulosABorrar) {
	            for (Autor autor : titulo.getAutores()) {
	                autor.getTitulos().remove(titulo);
	                autorDAO.save(autor);
	            }
	            titulo.setAutores(null); // o new HashSet<>() para limpiar la colección
	            if (titulo instanceof Libro) {
	                libroDAO.delete((Libro) titulo);
	            } else if (titulo instanceof Revista) {
	                revistaDAO.delete((Revista) titulo);
	            } else {
	                // Si es un tipo de Titulo que no es ni libro ni revista
	                tituloDAO.delete(titulo);
	            }
	        }
	        redirectAttributes.addFlashAttribute("mensaje", "Titulos con ISBN " + isbn + " borrados exitosamente");
	    } else {
	        redirectAttributes.addFlashAttribute("error", "No se encontró ningún titulo con ISBN " + isbn);
	    }

	    return "result3"; 
	}



	@GetMapping("/altaEjemplar")
	public String mostrarAltaEjemplarForm(Model model) {
		List<Titulo> todosTitulos = tituloDAO.findAll(); // obtenemos todos los títulos disponibles
		model.addAttribute("todosTitulos", todosTitulos); // pasamos la lista de títulos al modelo
		model.addAttribute("altaEjemplar", new Ejemplar()); // pasamos un nuevo Ejemplar al modelo
		return "altaEjemplar"; // como antes el nombre de la vista que muestra el formulario de alta ejemplar
	}

	@PostMapping("/altaEjemplar")
	public String altaEjemplar(@RequestParam("id") Long id, @ModelAttribute Ejemplar ejemplar, @ModelAttribute Titulo titulo, RedirectAttributes redirectAttributes, Model model) {
		model.addAttribute("ejemplar", ejemplar);
		model.addAttribute("titulo", titulo);
		titulo = tituloDAO.findById(id).orElseThrow(() -> new RuntimeException("Titulo no encontrado"));
		if (titulo != null) {
			Ejemplar nuevoEjemplar = new Ejemplar(titulo);
			ejemplarDAO.save(nuevoEjemplar);
			log.info("Nuevo ejemplar creado y asociado al título: " + titulo.getTitulo_obra());
			redirectAttributes.addFlashAttribute("ejemplarAgregado", true);
			redirectAttributes.addFlashAttribute("mensaje", "Nuevo ejemplar agregado exitosamente.");
			return "result4"; // redirigimos a la página de confirmación
		} else {
			log.error("El título no existe.");
			redirectAttributes.addFlashAttribute("ejemplarAgregado", false);
			redirectAttributes.addFlashAttribute("error", "El título no existe.");
			return "redirect:/altaEjemplar";
		}
		
	}

	@GetMapping("/bajaEjemplar")
	public String mostrarBajaEjemplarForm(Model model) {
		List<Ejemplar> todosEjemplares = ejemplarDAO.findAll(); // Obtener todos los ejemplares disponibles
		model.addAttribute("todosEjemplares", todosEjemplares); // Pasar la lista de ejemplares al modelo
		return "bajaEjemplar"; // Nombre de la vista que muestra el formulario de baja ejemplar
	}

	@PostMapping("/bajaEjemplar")
	public String bajaEjemplar(@RequestParam("idsEjemplares") List<Long> idsEjemplares,
			RedirectAttributes redirectAttributes) {
		for (Long id : idsEjemplares) {
			Ejemplar ejemplar = ejemplarDAO.findById(id).orElse(null); // Encontrar el ejemplar por ID
			if (ejemplar != null) {
				// Realiza la lógica para dar de baja el ejemplar, por ejemplo, eliminarlo de la
				// base de datos
				ejemplarDAO.delete(ejemplar);
				log.info("Ejemplar dado de baja con éxito.");
			}
		}
		redirectAttributes.addFlashAttribute("ejemplarDadoDeBaja", true);
		redirectAttributes.addFlashAttribute("mensaje", "Ejemplar(es) dado(s) de baja exitosamente.");
		return "result5"; // Redirigir a la página de confirmación
	}

}