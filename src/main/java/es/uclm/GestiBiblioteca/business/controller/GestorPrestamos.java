package es.uclm.GestiBiblioteca.business.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.uclm.GestiBiblioteca.business.entities.Ejemplar;
import es.uclm.GestiBiblioteca.business.entities.Prestamo;
import es.uclm.GestiBiblioteca.business.entities.Reserva;
import es.uclm.GestiBiblioteca.business.entities.Titulo;
import es.uclm.GestiBiblioteca.business.entities.Usuario;
import es.uclm.GestiBiblioteca.persistence.EjemplarDAO;
import es.uclm.GestiBiblioteca.persistence.PrestamoDAO;
import es.uclm.GestiBiblioteca.persistence.ReservaDAO;
import es.uclm.GestiBiblioteca.persistence.TituloDAO;
import es.uclm.GestiBiblioteca.persistence.UsuarioDAO;
import es.uclm.GestiBiblioteca.services.PenalizacionService;
import es.uclm.GestiBiblioteca.services.PrestamoService;


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
	@Autowired
	EjemplarDAO ejemplarDAO;
	@Autowired
	private PenalizacionService penalizacionService;
	@Autowired
	private PrestamoService prestamoService;
	private static final Logger log = (Logger) LoggerFactory.getLogger(GestorPrestamos.class);

	@GetMapping("/altaPrestamos")
	public String mostrarFormularioPrestamo(Model model) {
		List<Usuario> usuarios = usuarioDAO.findAll();
		List<Ejemplar> ejemplaresDisponibles = ejemplarDAO.findByDisponibleTrue();

		log.info("Usuarios: " + usuarios);

		model.addAttribute("usuarios", usuarios);
		model.addAttribute("ejemplares", ejemplaresDisponibles);

		return "altaPrestamos";
	}

	@PostMapping("/realizarPrestamo")
    public String realizarPrestamo(@ModelAttribute Prestamo prestamo, Model model, RedirectAttributes redirectAttributes) {
        try {
            prestamoService.realizarPrestamo(prestamo);
            model.addAttribute("prestamoRealizado", true);
            model.addAttribute("mensaje", "Préstamo realizado exitosamente.");
            log.info("Prestamo realizado exitosamente.");
            return "/resultPrestamo";
        } catch (Exception e) {
            log.error("Error al realizar el préstamo: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/rutaErrorPrestamo";
        }
    }

	@GetMapping("/rutaErrorPrestamo")
	public String mostrarErrorPrestamo(Model model) {
	    return "errorPrestamo";
	}

	@GetMapping("/devolucionEjemplar")
	public String mostrarFormularioDevolucion(Model model) {
	    List<Prestamo> prestamosActivos = prestamoDAO.findByActivoTrue();
	    log.debug("Número de préstamos activos: {}", prestamosActivos.size());
	    model.addAttribute("prestamos", prestamosActivos);
	    return "devolucionEjemplar";
	}




	@PostMapping("/realizarDevolucion")
	public String realizarDevolucion(@RequestParam Integer idPrestamo, RedirectAttributes redirectAttributes) {
	    try {
	        Optional<Prestamo> prestamoOpt = prestamoDAO.findById(idPrestamo);
	        if (prestamoOpt.isPresent()) {
	            Prestamo prestamo = prestamoOpt.get();
	            prestamo.setActivo(false); // Marca el préstamo como inactivo

	            // Verifica si la devolución es tardía y aplica penalizaciones
	            if (new Date().after(prestamo.getFechaFin())) {
	                penalizacionService.aplicarPenalizacion(prestamo.getUsuario());
	                redirectAttributes.addFlashAttribute("mensajePenalizacion", "Se ha aplicado una penalización por devolución tardía.");
	            }

	            // Marcar el ejemplar como disponible nuevamente
	            Ejemplar ejemplar = prestamo.getEjemplar(); // Asumiendo que puedes obtener el ejemplar desde el préstamo
	            ejemplar.setDisponible(true);
	            ejemplarDAO.save(ejemplar);

	            prestamoDAO.save(prestamo);
	            redirectAttributes.addFlashAttribute("ejemplarDevuelto", true);
	            redirectAttributes.addFlashAttribute("mensaje", "Devolución realizada exitosamente.");
	        } else {
	            log.error("Préstamo no encontrado con ID: " + idPrestamo);
	            redirectAttributes.addFlashAttribute("ejemplarDevuelto", false);
	            redirectAttributes.addFlashAttribute("mensaje", "Préstamo no encontrado.");
	        }
	    } catch (Exception e) {
	        log.error("Error al realizar la devolución: ", e);
	        redirectAttributes.addFlashAttribute("ejemplarDevuelto", false);
	        redirectAttributes.addFlashAttribute("mensaje", "Error al realizar la devolución.");
	    }
	    return "redirect:/resultadoDevolucion";
	}


	@GetMapping("/resultadoDevolucion")
	public String mostrarResultadoDevolucion() {
		return "resultadoDevolucion"; 
	}

	
	@GetMapping("/formularioReserva")
	public String mostrarFormularioReserva(Model model) {
	    List<Ejemplar> ejemplaresNoDisponibles = ejemplarDAO.findByDisponibleFalse();
	    List<Usuario> usuarios = usuarioDAO.findAll();

	    model.addAttribute("ejemplares", ejemplaresNoDisponibles);
	    model.addAttribute("usuarios", usuarios);

	    return "formularioReserva";
	}

	@PostMapping("/reservarEjemplar")
	public String reservarEjemplar(@RequestParam Long idEjemplar, @RequestParam Long idUsuario, RedirectAttributes redirectAttributes) {
	    Optional<Ejemplar> ejemplarOpt = ejemplarDAO.findById(idEjemplar);
	    Optional<Usuario> usuarioOpt = usuarioDAO.findById(idUsuario);

	    // Comprueba si el ejemplar y el usuario existen y si el ejemplar no está disponible
	    if (ejemplarOpt.isPresent() && usuarioOpt.isPresent() && !ejemplarOpt.get().isDisponible()) {
	        Ejemplar ejemplar = ejemplarOpt.get();
	        Usuario usuario = usuarioOpt.get();
	        
	        // Aquí ya no necesitas buscar el título, pues puedes obtenerlo directamente del ejemplar
	        Titulo titulo = ejemplar.getTitulo();

	        // Solo procede si el título no es null
	        if (titulo != null) {
	            Reserva reserva = new Reserva(usuario, ejemplar, titulo, new Date());
	            reservaDAO.save(reserva);
	            redirectAttributes.addFlashAttribute("mensajeExito", "Reserva realizada con éxito.");
	        } else {
	            redirectAttributes.addFlashAttribute("mensajeError", "El ejemplar no tiene un título asociado.");
	        }
	    } else {
	        String mensajeError = "Ejemplar no disponible o usuario no encontrado.";
	        if (ejemplarOpt.isPresent() && ejemplarOpt.get().isDisponible()) {
	            mensajeError = "El ejemplar está disponible y no se puede reservar.";
	        }
	        redirectAttributes.addFlashAttribute("mensajeError", mensajeError);
	    }

	    return "redirect:/rutaResultadoReserva";
	}



	@GetMapping("/rutaResultadoReserva")
	public String mostrarResultadoReserva(Model model) {
	    return "resultadoReserva"; // Nombre del archivo HTML del resultado
	}

}