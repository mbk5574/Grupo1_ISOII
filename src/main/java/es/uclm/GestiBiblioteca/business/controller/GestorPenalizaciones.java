package es.uclm.GestiBiblioteca.business.controller;

import es.uclm.GestiBiblioteca.business.entities.Usuario;
import es.uclm.GestiBiblioteca.persistence.UsuarioDAO;

public class GestorPenalizaciones {

	UsuarioDAO usuarioDAO;

	/**
	 * 
	 * @param u
	 */
	@GetMapping("/aplicarPenalizacion")
	public String mostrarFormularioPenalizaciones(Model model) {
	    List<Usuario> usuarios = usuarioDAO.findAll();
	    
	    log.info("Usuarios: " + usuarios);

	    model.addAttribute("usuarios", usuarios);

	    return "/aplicarPenalizacion"; 
	}

	@PostMapping("/aplicarPenalizacion")
	public String aplicarPenalizacion(@ModelAttribute("usuario") Usuario u, 
	                                   @RequestParam("fechaPenalizacion") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaPenalizacion,
	                                   Model model) { 
	    
	    try {
	        // Cargar al usuario desde la base de datos utilizando el ID del usuario
	        Optional<Usuario> optionalUser = usuarioDAO.findById(u.getId());
	        
	        if (optionalUser.isPresent()) {
	            Usuario existingUser = optionalUser.get();

	            // Aplicar la penalizaci�n (actualizar fecha de penalizaci�n)
	            existingUser.setFechaFinPenalizacion(fechaPenalizacion);

	            // Guardar el usuario actualizado de nuevo en la base de datos
	            usuarioDAO.save(existingUser);

	            // Devolver un mensaje de �xito
	            model.addAttribute("mensaje", "Penalizaci�n realizada exitosamente.");
	            log.info("Penalizaci�n aplicada a Usuario con ID: " + existingUser.getId());
	        } else {
	            // Manejar el caso en el que no se encuentra al usuario con el ID proporcionado
	            model.addAttribute("mensaje", "No se ha podido aplicar la penalizaci�n, ID no encontrado.");
	        }
	    } catch (Exception e) {
	        //  errores de base de datos
	        log.error("Error aplicando penalizaci�n", e);
	        model.addAttribute("mensaje", "Error al aplicar la penalizaci�n.");
	    }

	    return "/resultPenalizacion"; 
	}

	/**
	 * 
	 * @param u
	 */
	public void comprobarPenalizacion(Usuario u) {
		// TODO - implement GestorPenalizaciones.comprobarPenalizaci�n
		throw new UnsupportedOperationException();
	}

}
