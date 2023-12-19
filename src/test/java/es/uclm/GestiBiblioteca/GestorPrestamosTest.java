package es.uclm.GestiBiblioteca;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


import java.util.Date;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.uclm.GestiBiblioteca.business.controller.GestorPrestamos;
import es.uclm.GestiBiblioteca.business.entities.Ejemplar;
import es.uclm.GestiBiblioteca.business.entities.Prestamo;
import es.uclm.GestiBiblioteca.business.entities.Reserva;
import es.uclm.GestiBiblioteca.business.entities.Titulo;
import es.uclm.GestiBiblioteca.business.entities.Usuario;
import es.uclm.GestiBiblioteca.persistence.EjemplarDAO;
import es.uclm.GestiBiblioteca.persistence.PrestamoDAO;
import es.uclm.GestiBiblioteca.persistence.ReservaDAO;
import es.uclm.GestiBiblioteca.persistence.UsuarioDAO;
import es.uclm.GestiBiblioteca.services.PenalizacionService;
import es.uclm.GestiBiblioteca.services.PrestamoService;



@ExtendWith(MockitoExtension.class)
public class GestorPrestamosTest {

    
    @Mock
    private PrestamoDAO prestamoDAO;

    @Mock
    private EjemplarDAO ejemplarDAO;

    @Mock
    private UsuarioDAO  usuarioDAO;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private GestorPrestamos gestorPrestamos;

    @Mock
    private ReservaDAO reservaDAO;
    
     @Mock
    private PrestamoService prestamoService;

    @Mock
    private Prestamo prestamo;

    @Mock
    private  PenalizacionService penalizacion;
    
    @BeforeEach
    void setUp() {
        
    }
    @Test
    void realizarPrestamoTest() throws Exception {
        // Configuración de datos de prueba
        Prestamo prestamo = new Prestamo();
        prestamo.setEjemplar(new Ejemplar());
        prestamo.setUsuario(new Usuario()); 
        prestamo.setFechaFin(Date.from(new Date().toInstant().plusSeconds(86400)));
        prestamo.setActivo(true);
        prestamo.setFechaInicio(new Date());

        // Llamada al método bajo prueba
        String result = gestorPrestamos.realizarPrestamo(prestamo, model, redirectAttributes);

        // Verificaciones
        assertEquals("/resultadoAltaPrestamo", result);

        // Verificar las interacciones
        verify(prestamoService).realizarPrestamo(prestamo);
        verify(model).addAttribute("prestamoRealizado", true);
        verify(model).addAttribute("mensaje", "Préstamo realizado exitosamente.");
        verifyNoInteractions(redirectAttributes);
    }
    
    @Test
    void realizarPrestamoTestFechainiciosuperiorAFechafinal() throws Exception {
        // Configuración de datos de prueba
        Prestamo prestamo = new Prestamo();
        prestamo.setEjemplar(new Ejemplar());
        prestamo.setUsuario(new Usuario()); 
        prestamo.setFechaFin(Date.from(new Date().toInstant().plusSeconds(86400)));
        prestamo.setActivo(true);
        prestamo.setFechaInicio(Date.from(new Date().toInstant().plusSeconds(96400)));
       
        // Llamada al método bajo prueba
        String result = gestorPrestamos.realizarPrestamo(prestamo, model, redirectAttributes);

        // Verificaciones
        assertEquals("redirect:/rutaErrorPrestamo", result);
        redirectAttributes.addFlashAttribute("error", "La fecha de inicio no puede ser posterior a la fecha de fin.");
        verifyNoInteractions(model);;
    }
    @Test
    void realizarPrestamoConErrorTest() throws Exception {
        // Configuración de datos de prueba
        Prestamo prestamo = new Prestamo();
        prestamo.setFechaInicio(new Date());
        prestamo.setFechaFin(Date.from(new Date().toInstant().plusSeconds(86400))); 
        String mensajeError = "Error al realizar el préstamo";
        doThrow(new RuntimeException(mensajeError)).when(prestamoService).realizarPrestamo(prestamo);

        // Llamada al método bajo prueba
        String result = gestorPrestamos.realizarPrestamo(prestamo, model, redirectAttributes);

        // Verificaciones
        assertEquals("redirect:/rutaErrorPrestamo", result);

        // Verificar las interacciones
        verify(prestamoService).realizarPrestamo(prestamo);
        verify(redirectAttributes).addFlashAttribute("error", mensajeError);
        verifyNoInteractions(model);
    }
    @Test
    void realizarDevolucionTestEnTiempo() {
        // Configurar el comportamiento del mock para el findById de prestamoDAO
        Prestamo prestamo = new Prestamo( );
        prestamo.setActivo(true);
        prestamo.setEjemplar(new Ejemplar());
        prestamo.setFechaFin(Date.from(new Date().toInstant().plusSeconds(86400)));
        when(prestamoDAO.findById(any())).thenReturn(Optional.of(prestamo));

        // Realizar la devolución
        String result = gestorPrestamos.realizarDevolucion(1, redirectAttributes);

        // Verificar el resultado
        assertEquals("redirect:/resultadoDevolucion", result);

        // Verificar las interacciones
        verify(prestamoDAO).findById(1);
        verify(prestamoDAO).save(prestamo);
        
        verify(redirectAttributes).addFlashAttribute("ejemplarDevuelto", true);
        
        verify(redirectAttributes).addFlashAttribute("mensaje", "Devolución realizada exitosamente.");
    }
    
    @Test
    void realizarDevolucionTestAfterEndDate() {
        // Configuración del mock para findById de prestamoDAO
        Prestamo prestamo = new Prestamo();
        prestamo.setActivo(true);
        prestamo.setEjemplar(new Ejemplar());
        prestamo.setFechaFin(Date.from(new Date().toInstant().minusSeconds(86400)));
        prestamo.setUsuario(new Usuario());
        when(prestamoDAO.findById(anyInt())).thenReturn(Optional.of(prestamo));
        // Realizar la devolución
        String result = gestorPrestamos.realizarDevolucion(1, redirectAttributes);

        // Verificar el resultado
        assertEquals("redirect:/resultadoDevolucion", result);

        // Verificar las interacciones
        verify(prestamoDAO).findById(1);
        verify(redirectAttributes).addFlashAttribute("mensajePenalizacion", "Se ha aplicado una penalización por devolución tardía.");
        verify(redirectAttributes).addFlashAttribute("ejemplarDevuelto", true);
        verify(redirectAttributes).addFlashAttribute("mensaje", "Devolución realizada exitosamente.");
    }

   

    @Test
    void realizarDevolucionPrestamoNoEncontradoTest() {
        // Configurar el comportamiento del mock para el findById de prestamoDAO
        when(prestamoDAO.findById(any())).thenReturn(Optional.empty());

        // Realizar la devolución
        String result = gestorPrestamos.realizarDevolucion(1, redirectAttributes);

        // Verificar el resultado
        assertEquals("redirect:/resultadoDevolucion", result);

        // Verificar las interacciones
        verify(prestamoDAO).findById(1);
        verify(redirectAttributes).addFlashAttribute("ejemplarDevuelto", false);
        verify(redirectAttributes).addFlashAttribute("mensaje", "Préstamo no encontrado.");
    }
    

    @Test
    void reservarEjemplarTestDisponibleNoReserva() {
        // Configuración de datos de prueba
        Long idEjemplar = 1L;
        Long idUsuario = 2L;

        // Crear ejemplar y usuario de prueba
        Ejemplar ejemplar = new Ejemplar();
        ejemplar.setId(idEjemplar);
        ejemplar.setDisponible(true);
        ejemplar.setTitulo(new Titulo());
        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);

        // Configurar el comportamiento de los mocks
        when(ejemplarDAO.findById(eq(idEjemplar))).thenReturn(Optional.of(ejemplar));
        when(usuarioDAO.findById(eq(idUsuario))).thenReturn(Optional.of(usuario));

        // Llamada al método bajo prueba
        String result = gestorPrestamos.reservarEjemplar(idEjemplar, idUsuario, redirectAttributes);

         // Verificaciones
        assertEquals("redirect:/rutaResultadoReserva", result);

        // Verificar las interacciones
        verify(ejemplarDAO).findById(idEjemplar);
        verify(usuarioDAO).findById(idUsuario);

        // Verificar que no se intentó hacer una reserva si el ejemplar está disponible
        verifyNoInteractions(reservaDAO);
        verify(redirectAttributes).addFlashAttribute("mensajeError", "El ejemplar está disponible y no se puede reservar.");
       
    }
    @Test
    void reservarEjemplarTestExito() {
        // Configuración de datos de prueba
        Long idEjemplar = 1L;
        Long idUsuario = 2L;

        // Crear ejemplar y usuario de prueba
        Ejemplar ejemplar = new Ejemplar();
        ejemplar.setId(idEjemplar);
        ejemplar.setDisponible(false);
        ejemplar.setTitulo(new Titulo());
        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);

        // Configurar el comportamiento de los mocks
        when(ejemplarDAO.findById(eq(idEjemplar))).thenReturn(Optional.of(ejemplar));
        when(usuarioDAO.findById(eq(idUsuario))).thenReturn(Optional.of(usuario));

        // Llamada al método bajo prueba
        String result = gestorPrestamos.reservarEjemplar(idEjemplar, idUsuario, redirectAttributes);

         // Verificaciones
        assertEquals("redirect:/rutaResultadoReserva", result);

        // Verificar las interacciones
        verify(ejemplarDAO).findById(idEjemplar);
        verify(usuarioDAO).findById(idUsuario);

        // Verificar que no se intentó hacer una reserva si el ejemplar está disponible
        verify(reservaDAO).save(any(Reserva.class));
        verify(redirectAttributes).addFlashAttribute("mensajeExito", "Reserva realizada con éxito.");
    }
    @Test
    void reservarEjemplarTestEjemplarNoEncontrado() {
        // Configuración de datos de prueba
        Long idEjemplar = 1L;
        Long idUsuario = 2L;

        // Crear usuario de prueba
        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);

        // Configurar el comportamiento de los mocks
        when(ejemplarDAO.findById(eq(idEjemplar))).thenReturn(Optional.empty());
        when(usuarioDAO.findById(eq(idUsuario))).thenReturn(Optional.of(usuario));

        // Llamada al método bajo prueba
        String result = gestorPrestamos.reservarEjemplar(idEjemplar, idUsuario, redirectAttributes);

        // Verificaciones
        assertEquals("redirect:/rutaResultadoReserva", result);

        // Verificar las interacciones
        verify(ejemplarDAO).findById(idEjemplar);
        verify(usuarioDAO).findById(idUsuario);

        // Verificar que no se intentó hacer una reserva si el ejemplar no se encuentra
        verifyNoInteractions(reservaDAO);
        verify(redirectAttributes).addFlashAttribute("mensajeError", "Ejemplar no disponible o usuario no encontrado.");
    }
    @Test
    void reservarEjemplarTestEjemplarNoSePuedeReservar() {
        // Configuración de datos de prueba
        Long idEjemplar = 1L;
        Long idUsuario = 2L;
        
        Ejemplar ejemplar = new Ejemplar();
        ejemplar.setId(idEjemplar);
        ejemplar.setDisponible(false);
        ejemplar.setTitulo(null);
        // Crear usuario de prueba
        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);

        // Configurar el comportamiento de los mocks
        when(ejemplarDAO.findById(eq(idEjemplar))).thenReturn(Optional.of(ejemplar));
        when(usuarioDAO.findById(eq(idUsuario))).thenReturn(Optional.of(usuario));

        // Llamada al método bajo prueba
        String result = gestorPrestamos.reservarEjemplar(idEjemplar, idUsuario, redirectAttributes);

        // Verificaciones
        assertEquals("redirect:/rutaResultadoReserva", result);

        // Verificar las interacciones
        verify(ejemplarDAO).findById(idEjemplar);
        verify(usuarioDAO).findById(idUsuario);

        // Verificar que no se intentó hacer una reserva si el ejemplar no se encuentra
        verifyNoInteractions(reservaDAO);
        verify(redirectAttributes).addFlashAttribute("mensajeError", "El ejemplar no tiene un título asociado.");
    }
}
