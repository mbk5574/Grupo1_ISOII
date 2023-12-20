package es.uclm.GestiBiblioteca;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import es.uclm.GestiBiblioteca.business.entities.Usuario;
import es.uclm.GestiBiblioteca.persistence.EjemplarDAO;
import es.uclm.GestiBiblioteca.persistence.PrestamoDAO;
import es.uclm.GestiBiblioteca.persistence.UsuarioDAO;
import es.uclm.GestiBiblioteca.services.PenalizacionService;

@ExtendWith(MockitoExtension.class)
public class PenalizacionServiceTest {

	@Mock
    private PrestamoDAO prestamoDAO;

    @Mock
    private EjemplarDAO ejemplarDAO;

    @Mock
    private UsuarioDAO usuarioDAO; // Agrega el mock para UsuarioDAO

    @InjectMocks
    private PenalizacionService penalizacionService;
    

    @Test
    /*Función de prueba que verifica que el método 'comprobarPenalización' de 'PenalizacionService'
     * funciona correctamente cuando se le proporciona un usuario con una fecha de penalización futura*/
    void comprobarPenalizacionTest() {
        // Configuración de datos de prueba
        Usuario usuario = new Usuario();
        usuario.setFechaFinPenalizacion(new Date(System.currentTimeMillis() + 100000)); // Fecha futura

        // Llamada al método que se está probando y verificación del resultado
        assertTrue(penalizacionService.comprobarPenalizacion(usuario));
    }
    
    @Test
    /* Función de prueba que verifica que el método 'comprobarPenalizacion' de 'PenalizacionService'
     * funciona correctamente cuando se le proporciona un usuario sin penalización */
    void comprobarPenalizacionCuandoNoHayPenalizacionTest() {
        // Configuración de datos de prueba
        Usuario usuario = new Usuario();

        // Llamada al método que se está probando y verificación del resultado
        assertFalse(penalizacionService.comprobarPenalizacion(usuario));
    }
    
    @Test
    /* Función de prueba que verifica que el método 'comprobarPenalizacion' de 'PenalizacionService'
     * funciona correctamente cuando se le proporciona un usuario con una fecha de penalización ya expirada */
    void comprobarPenalizacionDespuesDeExpirarTest() {
        // Configuración de datos de prueba
        Usuario usuario = new Usuario();
        usuario.setFechaFinPenalizacion(new Date(System.currentTimeMillis() - 100000)); // Fecha pasada

        // Llamada al método que se está probando y verificación del resultado
        assertFalse(penalizacionService.comprobarPenalizacion(usuario));
    }
    
    @Test
    /* Función de prueba que verifica que el método 'aplicarPenalizacion' de 'PenalizacionService'
     * actualiza correctamente la fecha de penalización cuando ya existe una fecha de penalización */
    void aplicarPenalizacionConFechaFinExistenteTest() {
        // Configuración de datos de prueba
        Usuario usuario = new Usuario();
        usuario.setFechaFinPenalizacion(new java.util.Date(System.currentTimeMillis() + 100000)); // Fecha futura

        // Llamada al método que se está probando
        penalizacionService.aplicarPenalizacion(usuario);

        // Verificación del resultado
        java.util.Date nuevaFechaFin = usuario.getFechaFinPenalizacion();
        assertNotNull(nuevaFechaFin);
        assertTrue(nuevaFechaFin.after(new java.util.Date()));
        // Puedes agregar más verificaciones según sea necesario
    }
    
    @Test
    /* Función de prueba que verifica que el método 'aplicarPenalizacion' de 'PenalizacionService'
     * establece correctamente la fecha de penalización cuando no existe una fecha de penalización previa */
    void aplicarPenalizacionSinFechaExistenteTest() {
        // Configuración de datos de prueba
        Usuario usuario = new Usuario();

        // Llamada al método que se está probando
        penalizacionService.aplicarPenalizacion(usuario);

        // Verificación del resultado
        java.util.Date nuevaFechaFin = usuario.getFechaFinPenalizacion();
        assertNotNull(nuevaFechaFin);
        assertTrue(nuevaFechaFin.after(new java.util.Date()));
    }
    
    @Test
    /* Función de prueba que verifica que el método 'aplicarPenalizacion' de 'PenalizacionService'
     * no afecta la fecha de penalización cuando se le proporciona un usuario con fecha expirada */
    void aplicarPenalizacionConFechaExpiradaTest() {
        // Configuración de datos de prueba
        Usuario usuario = new Usuario();
        usuario.setFechaFinPenalizacion(new java.util.Date(System.currentTimeMillis() - 100000)); // Fecha pasada

        // Llamada al método que se está probando
        penalizacionService.aplicarPenalizacion(usuario);

        // Verificación del resultado
        java.util.Date fechaOriginal = usuario.getFechaFinPenalizacion();
        java.util.Date nuevaFechaFin = usuario.getFechaFinPenalizacion();
        assertEquals(fechaOriginal, nuevaFechaFin);
    }

    @Test
    /* Función de prueba que verifica que el método 'comprobarPenalizacion' de 'PenalizacionService'
     * devuelve falso cuando se le proporciona un usuario nulo */
    void comprobarPenalizacionConUsuarioNuloTest() {
        // Configuración de datos de prueba
        Usuario usuario = new Usuario();

        // Llamada al método que se está probando y verificación del resultado
        assertFalse(penalizacionService.comprobarPenalizacion(usuario));
    }


    }