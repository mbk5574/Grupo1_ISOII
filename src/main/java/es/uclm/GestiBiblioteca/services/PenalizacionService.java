package es.uclm.GestiBiblioteca.services;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import es.uclm.GestiBiblioteca.business.entities.Usuario;
import es.uclm.GestiBiblioteca.persistence.UsuarioDAO;

@Controller
public class PenalizacionService {

	private static final int DIAS_DE_PENALIZACION = 5; // Número de días de penalización
	@Autowired
	UsuarioDAO usuarioDAO;

	@Transactional
	public void aplicarPenalizacion(Usuario usuario) {
        Calendar cal = Calendar.getInstance();
        if (usuario.getFechaFinPenalizacion() != null && usuario.getFechaFinPenalizacion().after(new Date())) {
            cal.setTime(usuario.getFechaFinPenalizacion());
        }
        cal.add(Calendar.DAY_OF_MONTH, DIAS_DE_PENALIZACION);
        usuario.setFechaFinPenalizacion(cal.getTime());
        usuarioDAO.save(usuario);
    }

	public boolean comprobarPenalizacion(Usuario usuario) {
        Date hoy = new Date();
        return usuario.getFechaFinPenalizacion() != null && usuario.getFechaFinPenalizacion().after(hoy);
    }

}