package es.uclm.GestiBiblioteca.business.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.stereotype.Controller;
import es.uclm.GestiBiblioteca.business.entities.Usuario;
import es.uclm.GestiBiblioteca.persistence.UsuarioDAO;

@Controller
public class GestorUsuarios {

	@Autowired
	UsuarioDAO usuarioDAO;

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

}

