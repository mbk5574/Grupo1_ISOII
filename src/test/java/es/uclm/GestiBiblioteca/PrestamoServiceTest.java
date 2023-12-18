package es.uclm.GestiBiblioteca;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.uclm.GestiBiblioteca.business.entities.Ejemplar;
import es.uclm.GestiBiblioteca.business.entities.Prestamo;
import es.uclm.GestiBiblioteca.business.entities.Reserva;
import es.uclm.GestiBiblioteca.business.entities.Titulo;
import es.uclm.GestiBiblioteca.business.entities.Usuario;
import es.uclm.GestiBiblioteca.persistence.AutorDAO;
import es.uclm.GestiBiblioteca.persistence.EjemplarDAO;
import es.uclm.GestiBiblioteca.persistence.LibroDAO;
import es.uclm.GestiBiblioteca.persistence.PrestamoDAO;
import es.uclm.GestiBiblioteca.persistence.ReservaDAO;
import es.uclm.GestiBiblioteca.persistence.RevistaDAO;
import es.uclm.GestiBiblioteca.persistence.TituloDAO;
import es.uclm.GestiBiblioteca.persistence.UsuarioDAO;
import es.uclm.GestiBiblioteca.services.PenalizacionService;
import es.uclm.GestiBiblioteca.services.PrestamoService;

@ExtendWith(MockitoExtension.class)

public class PrestamoServiceTest {

	@Mock
	private AutorDAO autorDAO;
	@Mock
	private TituloDAO tituloDAO;
	@Mock
	private PrestamoDAO prestamoDAO;
	@Mock
	private ReservaDAO reservaDAO;
	@Mock 
	private EjemplarDAO ejemplarDAO;
	@Mock 
	private LibroDAO libroDAO;
	@Mock
	private RevistaDAO revistaDAO;
	@Mock
	private UsuarioDAO usuarioDAO;
	@Mock
	private Ejemplar ejemplar1 = new Ejemplar();
	@Mock
	private Ejemplar ejemplar2 = new Ejemplar();
	@Mock
	private Ejemplar ejemplar3 = new Ejemplar();
	@Mock
	private Prestamo prestamo = new Prestamo();
	@Mock
	private Titulo titulo = new Titulo();
	@InjectMocks
	private PrestamoService prestamoService;
	@Mock
	private PenalizacionService penalizacionService;
	@Mock
	private Usuario usuario = new Usuario();
	
	
	@BeforeEach
	void setUp() {
	   
	}

	
	@Test
	public void puedePrestar_UsuarioConLimitePrestamos() {
	    Long usuarioId = 1L;
	    Usuario usuario = new Usuario();
	    usuario.setPrestamos(new HashSet<>());
	    
	    when(usuarioDAO.findById(usuarioId)).thenReturn(Optional.of(usuario));
	    when(prestamoDAO.countByUsuarioIdAndActivoTrue(usuarioId)).thenReturn(PrestamoService.LIMITE_DE_LIBROS);

	    String resultado = prestamoService.puedePrestar(usuarioId);

	    assertEquals("El usuario ha alcanzado el límite de préstamos permitidos.", resultado);
	}

	@Test
	public void puedePrestar_UsuarioConPenalizaciones() {
	    Long usuarioId = 1L;
	    Usuario usuario = new Usuario();
	    usuario.setPrestamos(new HashSet<>());
	    
	    when(usuarioDAO.findById(usuarioId)).thenReturn(Optional.of(usuario));
	    when(prestamoDAO.countByUsuarioIdAndActivoTrue(usuarioId)).thenReturn(PrestamoService.LIMITE_DE_LIBROS - 1);
	    when(penalizacionService.comprobarPenalizacion(usuario)).thenReturn(true);

	    String resultado = prestamoService.puedePrestar(usuarioId);

	    assertEquals("El usuario tiene penalizaciones activas.", resultado);
	}

	@Test
	public void puedePrestar_UsuarioPuedePedirPrestado() {
	    Long usuarioId = 1L;
	    Usuario usuario = new Usuario();
	    usuario.setPrestamos(new HashSet<>());
	    
	    when(usuarioDAO.findById(usuarioId)).thenReturn(Optional.of(usuario));
	    when(prestamoDAO.countByUsuarioIdAndActivoTrue(usuarioId)).thenReturn(PrestamoService.LIMITE_DE_LIBROS - 1);
	    when(penalizacionService.comprobarPenalizacion(usuario)).thenReturn(false);

	    String resultado = prestamoService.puedePrestar(usuarioId);

	    assertNull(resultado);
	}

	@Test
    public void realizarPrestamo_EjemplarNoDisponible() {
        
		when(ejemplar1.getId()).thenReturn(1L);
        when(prestamo.getEjemplar()).thenReturn(ejemplar1);
        when(ejemplarDAO.findById(1L)).thenReturn(Optional.of(ejemplar1));
        when(ejemplar1.isDisponible()).thenReturn(false);

        Exception exception = assertThrows(Exception.class, () -> {
            prestamoService.realizarPrestamo(prestamo);
        });

        assertEquals("El ejemplar no está disponible o no se encontró.", exception.getMessage());
        
    }
	
	@Test
	void realizarPrestamo_UsuarioNoPuedePrestar() throws Exception {
	    // Configurar los mocks necesarios para este test específico
	    when(prestamo.getEjemplar()).thenReturn(ejemplar1);
	    when(ejemplar1.getId()).thenReturn(1L);
	    when(ejemplarDAO.findById(1L)).thenReturn(Optional.of(ejemplar1));
	    when(ejemplar1.isDisponible()).thenReturn(true);

	    // Configurar el mock de prestamo.getUsuario() para devolver un usuario válido
	    when(prestamo.getUsuario()).thenReturn(usuario);
	    when(usuario.getId()).thenReturn(1L);
	    when(usuarioDAO.findById(1L)).thenReturn(Optional.of(usuario));
	    when(penalizacionService.comprobarPenalizacion(usuario)).thenReturn(true);

	    // Ejecutar el método a probar y capturar la excepción
	    Exception exception = assertThrows(Exception.class, () -> {
	        prestamoService.realizarPrestamo(prestamo);
	    });

	    // Asegurar que se obtiene el mensaje de error esperado
	    assertEquals("El usuario tiene penalizaciones activas.", exception.getMessage());
	}




	@Test
	void realizarPrestamo_Exito() throws Exception {
		
	    Ejemplar ejemplarMock = mock(Ejemplar.class);
	    Usuario usuarioMock = mock(Usuario.class);
	    Titulo tituloMock = mock(Titulo.class);
	    
	    when(prestamo.getEjemplar()).thenReturn(ejemplarMock);
	    when(prestamo.getUsuario()).thenReturn(usuarioMock);
	    when(usuarioMock.getId()).thenReturn(1L);
	    when(ejemplarMock.getId()).thenReturn(1L);
	    when(ejemplarMock.getTitulo()).thenReturn(tituloMock);
	    when(ejemplarMock.isDisponible()).thenReturn(true);
	    when(usuarioDAO.findById(1L)).thenReturn(Optional.of(usuarioMock));
	    when(ejemplarDAO.findById(1L)).thenReturn(Optional.of(ejemplarMock));
	    when(penalizacionService.comprobarPenalizacion(usuarioMock)).thenReturn(false);

	 // Simular que hay reservas asociadas al ejemplar
	    List<Reserva> reservas = new ArrayList<>();
	    Reserva reservaMock = mock(Reserva.class);
	    reservas.add(reservaMock);
	    when(reservaDAO.findByEjemplar(ejemplarMock)).thenReturn(reservas);

	    // Ejecutar el método a probar
	    prestamoService.realizarPrestamo(prestamo);

	    // Verificaciones
	    for (Reserva reserva : reservas) {
	        verify(reservaDAO).delete(reserva); // Verificar que se elimina cada reserva
	    }
	    
	    verify(ejemplarDAO).save(ejemplarMock);
	    verify(prestamoDAO).save(prestamo);
	
	}




	
	
	
}
