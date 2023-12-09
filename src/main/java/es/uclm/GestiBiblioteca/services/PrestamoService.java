package es.uclm.GestiBiblioteca.services;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uclm.GestiBiblioteca.business.entities.Ejemplar;
import es.uclm.GestiBiblioteca.business.entities.Prestamo;
import es.uclm.GestiBiblioteca.business.entities.Reserva;
import es.uclm.GestiBiblioteca.business.entities.Titulo;
import es.uclm.GestiBiblioteca.business.entities.Usuario;
import es.uclm.GestiBiblioteca.persistence.EjemplarDAO;
import es.uclm.GestiBiblioteca.persistence.PrestamoDAO;
import es.uclm.GestiBiblioteca.persistence.ReservaDAO;
import es.uclm.GestiBiblioteca.persistence.UsuarioDAO;

@Service
public class PrestamoService {
	
	private static final int LIMITE_DE_LIBROS = 5;
    private static final Logger log = LoggerFactory.getLogger(PrestamoService.class);
    @Autowired
    private EjemplarDAO ejemplarDAO;
    @Autowired
    private PrestamoDAO prestamoDAO;
    @Autowired
    private ReservaDAO reservaDAO;
    @Autowired
    private UsuarioDAO usuarioDAO;


    @Autowired
    private PenalizacionService penalizacionService;

    @Transactional(readOnly = true)
    public String puedePrestar(Long usuarioId) {
    	
        Usuario usuarioActualizado = usuarioDAO.findById(usuarioId).orElse(null);
        int numeroPrestamosActivos = prestamoDAO.countByUsuarioIdAndActivoTrue(usuarioId);

        if (usuarioActualizado.getPrestamos() != null && numeroPrestamosActivos >= LIMITE_DE_LIBROS) {
            log.info("El usuario ha alcanzado el límite de préstamos permitidos.");
            return "El usuario ha alcanzado el límite de préstamos permitidos.";
        }

        if (penalizacionService.comprobarPenalizacion(usuarioActualizado)) {
            log.info("El usuario tiene penalizaciones activas.");
            return "El usuario tiene penalizaciones activas.";
        }

        return null; // El usuario puede pedir prestado
    }
    
    @Transactional
    public void realizarPrestamo(Prestamo prestamo) throws Exception {
        Ejemplar ejemplarActualizado = ejemplarDAO.findById(prestamo.getEjemplar().getId()).orElse(null);

        if (ejemplarActualizado == null || !ejemplarActualizado.isDisponible()) {
            throw new Exception("El ejemplar no está disponible o no se encontró.");
        }
        
        String mensajeError = puedePrestar(prestamo.getUsuario().getId());
        
        if (mensajeError != null) {
            throw new Exception(mensajeError);
        }
        
        
        Titulo titulo = ejemplarActualizado.getTitulo();
        if (titulo == null) {
            throw new Exception("El ejemplar no tiene un título asociado.");
        }
        
        prestamo.setTitulo(titulo);

        ejemplarActualizado.setDisponible(false); // Marca el ejemplar como no disponible
        
     // Aquí es donde se elimina todas las reservas para ese ejemplar.
        List<Reserva> reservas = reservaDAO.findByEjemplar(ejemplarActualizado);
        for (Reserva reserva : reservas) {
            reservaDAO.delete(reserva);
        }
        
        ejemplarDAO.save(ejemplarActualizado);

        prestamo.setActivo(true); // Marca el préstamo como activo
        prestamoDAO.save(prestamo);
    }
}


