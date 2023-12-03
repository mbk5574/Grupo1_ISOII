package es.uclm.GestiBiblioteca.business.controller;

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

	
	@GetMapping("/registroUsuario")
	public String mostrarFormularioRegistroUsuario(Model model) {
	    model.addAttribute("nuevoUsuario", new Usuario());
	    return "registroUsuario"; 
	}

	@PostMapping("/registroUsuario")
	public String registrarUsuario(@ModelAttribute Usuario nuevoUsuario, RedirectAttributes redirectAttributes) {
	    try {
	        usuarioDAO.save(nuevoUsuario);
	        log.info("Usuario registrado: " + nuevoUsuario);
	        redirectAttributes.addFlashAttribute("mensajeExito", "Usuario registrado exitosamente.");
	    } catch (Exception e) {
	        log.error("Error al registrar el usuario: ", e);
	        redirectAttributes.addFlashAttribute("mensajeError", "Error al registrar el usuario.");
	    }
	    return "redirect:/resultadoRegistroUsuario";
	}
	
	@GetMapping("/resultadoRegistroUsuario")
	public String mostrarResultadoRegistro() {
	    return "resultadoRegistroUsuario";
	}


	
	
	@GetMapping("/altaPrestamos")
	public String mostrarFormularioPrestamo(Model model) {
		List<Usuario> usuarios = usuarioDAO.findAll();
		List<Titulo> titulos = tituloDAO.findAll();
		
	    log.info("Usuarios: " + usuarios);

		model.addAttribute("usuarios", usuarios);
		model.addAttribute("titulos", titulos);

		return "altaPrestamos";
	}

	@PostMapping("/realizar-prestamo")
	public String realizarPrestamo(@ModelAttribute Prestamo prestamo, Model model, RedirectAttributes redirectAttributes) {
		prestamoDAO.save(prestamo);
		log.info("Saved prestamo: " + prestamo);
		model.addAttribute("prestamoRealizado", true);
		model.addAttribute("mensaje", "Préstamo realizado exitosamente.");

		return "/resultPrestamo";
	}

	@GetMapping("/devolucionEjemplar")
	public String mostrarFormularioDevolucion(Model model) {
	    List<Prestamo> prestamosActivos = prestamoDAO.findByActivoTrue();
		model.addAttribute("prestamos", prestamosActivos);
		return "devolucionEjemplar"; 
	}

	@PostMapping("/realizar-devolucion")
	public String realizarDevolucion(@RequestParam Integer idPrestamo, RedirectAttributes redirectAttributes) {
		try {
			Optional<Prestamo> prestamoOpt = prestamoDAO.findById(idPrestamo);
			if (prestamoOpt.isPresent()) {
				Prestamo prestamo = prestamoOpt.get();
				prestamo.setActivo(false);
				prestamoDAO.save(prestamo);

				log.info("Devolución realizada exitosamente para el préstamo con ID: " + idPrestamo);
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

	/**
	 * 
	 * @param idUsuario
	 * @param isbn
	 */
public void realizarReserva(String idUsuario, String isbn) {
        // Obtener el usuario y el título de la base de datos
        //Usuario usuario = obtenerUsuarioPorId(idUsuario);
        //Titulo titulo = obtenerTituloPorISBN(isbn);

        // Verificar si el usuario y el título existen
        //if (usuario == null || titulo == null) {
            // Manejar la situación en la que el usuario o el título no existen
            throw new IllegalArgumentException("Usuario o título no encontrados");
        //}

        // Verificar si ya hay una reserva para este usuario y título
        //if (existeReserva(usuario, titulo)) {
            // Manejar la situación en la que ya hay una reserva
        //    throw new IllegalStateException("Ya existe una reserva para este usuario y título");
      //  }

        // Crear la reserva
        //Reserva reserva = new Reserva(usuario, titulo, new Date());

        // Guardar la reserva en la base de datos o realizar otras operaciones necesarias
        //guardarReservaEnBaseDeDatos(reserva);

        // Otra lógica que desees realizar después de realizar la reserva

        // Puedes lanzar excepciones específicas o devolver información adicional si es necesario
    //}

}
}