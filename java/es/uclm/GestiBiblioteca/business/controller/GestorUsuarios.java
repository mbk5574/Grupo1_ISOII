package es.uclm.GestiBiblioteca.business.controller;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import es.uclm.GestiBiblioteca.business.entities.Usuario;
import es.uclm.GestiBiblioteca.persistence.PrestamoDAO;
import es.uclm.GestiBiblioteca.persistence.UsuarioDAO;

@Controller
public class GestorUsuarios {


	@Autowired
	UsuarioDAO usuarioDAO;
	
	@GetMapping("/usuarios")
	public ResponseEntity<List<Usuario>> listarUsuarios() {

	    try {
	        List<Usuario> usuarios = usuarioDAO.findAll();
	        return ResponseEntity.ok(usuarios);
	    } catch (Exception e) {
	        return ResponseEntity.badRequest().build();
	    }
	}
	
	@PostMapping("/usuarios")
	public ResponseEntity<Usuario> añadirUsuario(@RequestBody Usuario usuario) {

	    try {
	        usuarioDAO.save(usuario);
	        return ResponseEntity.ok(usuario);
	    } catch (Exception e) {
	        return ResponseEntity.badRequest().build();
	    }
	}
}


