package es.uclm.GestiBiblioteca;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.eq;

import es.uclm.GestiBiblioteca.business.entities.Autor;
import es.uclm.GestiBiblioteca.business.entities.Ejemplar;
import es.uclm.GestiBiblioteca.business.entities.Libro;
import es.uclm.GestiBiblioteca.business.entities.Revista;
import es.uclm.GestiBiblioteca.business.entities.Titulo;
import es.uclm.GestiBiblioteca.persistence.AutorDAO;
import es.uclm.GestiBiblioteca.persistence.EjemplarDAO;
import es.uclm.GestiBiblioteca.persistence.LibroDAO;
import es.uclm.GestiBiblioteca.persistence.PrestamoDAO;
import es.uclm.GestiBiblioteca.persistence.ReservaDAO;
import es.uclm.GestiBiblioteca.persistence.RevistaDAO;
import es.uclm.GestiBiblioteca.persistence.TituloDAO;
import es.uclm.GestiBiblioteca.services.TituloService;

@ExtendWith(MockitoExtension.class)
public class TituloServiceTest {


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
	private Ejemplar ejemplar1 = new Ejemplar();
	@Mock
	private Ejemplar ejemplar2 = new Ejemplar();
	@Mock
	private Ejemplar ejemplar3 = new Ejemplar();
	@Mock
	private Titulo titulo = new Titulo();
	@Mock
	private Ejemplar ejemplarIndisponible; 
	@InjectMocks
	private TituloService tituloService;
	@Mock
    private ArrayList<Ejemplar> ejemplares;
	
    private Ejemplar ejemplar;
	
    private Long ejemplarId;
    
    private String[] autoresNombres;
    private Collection<Autor> autores;

	@BeforeEach
	void setUp() {
		ejemplares = new ArrayList<>();
		ejemplares.add(ejemplar1); // Asegúrate de que ejemplar es un mock válido
		ejemplares.add(ejemplar2);
		ejemplares.add(ejemplar3);
		ejemplarIndisponible = new Ejemplar();
        ejemplar = new Ejemplar();
        ejemplarId = 1L;
        autoresNombres = new String[] {"Autor1", " Autor2 ", "Autor3"};
        autores = new ArrayList<>();
	}
	
	
	@Test
    public void eliminarTituloConVerificaciones_TituloExistente_EliminacionExitosa() {
		
        Long tituloId = 1L;
        boolean activo = true; 

        when(tituloDAO.findById(tituloId)).thenReturn(Optional.of(titulo));
        when(titulo.getEjemplares()).thenReturn(ejemplares);
        when(prestamoDAO.existsByEjemplarAndActivo((ejemplar1), activo)).thenReturn(false);
        when(reservaDAO.existsByEjemplar(eq(ejemplar1))).thenReturn(false);

        boolean resultado = tituloService.eliminarTituloConVerificaciones(tituloId);

        assertTrue(resultado, "El título debería haberse eliminado exitosamente");

        verify(ejemplarDAO, times(1)).delete(ejemplar1);
        verify(ejemplarDAO, times(1)).delete(ejemplar2);
        verify(ejemplarDAO, times(1)).delete(ejemplar3);
        verify(tituloDAO, times(1)).delete(titulo);
        
    }

	
	@Test
	public void eliminarTituloConVerificaciones_TituloNoExistente() {
	    Long tituloIdInexistente = 2L; 

	    when(tituloDAO.findById(tituloIdInexistente)).thenReturn(Optional.empty()); 

	    boolean resultado = tituloService.eliminarTituloConVerificaciones(tituloIdInexistente);

	    assertFalse(resultado, "El método debería devolver false ya que el título no existe");

	    verify(tituloDAO, times(0)).delete((titulo));
	    verify(ejemplarDAO, times(0)).delete((ejemplar2));
	}

	
	@Test
	public void eliminarTituloConVerificaciones_AlgunEjemplarNoDisponible() {
	    Long tituloId = 1L;
	    when(tituloDAO.findById(tituloId)).thenReturn(Optional.of(titulo));
	    when(titulo.getEjemplares()).thenReturn(Arrays.asList(ejemplar1, ejemplar2, ejemplar3, ejemplarIndisponible));

	    // Mocks para cada ejemplar
	    when(prestamoDAO.existsByEjemplarAndActivo(ejemplar1, true)).thenReturn(false);
	    when(prestamoDAO.existsByEjemplarAndActivo(ejemplar2, true)).thenReturn(false);
	    when(prestamoDAO.existsByEjemplarAndActivo(ejemplar3, true)).thenReturn(false);
	    when(prestamoDAO.existsByEjemplarAndActivo(ejemplarIndisponible, true)).thenReturn(true);

	    boolean resultado = tituloService.eliminarTituloConVerificaciones(tituloId);

	    assertFalse(resultado, "El método debería devolver false ya que hay al menos un ejemplar no disponible");

	    verify(tituloDAO, times(0)).delete(titulo);
	}
	
	@Test
    void eliminarTituloYAutores_TituloEsNull() {
        boolean resultado = tituloService.eliminarTituloYAutores(null);
        assertFalse(resultado);
    }

    @Test
    void eliminarTituloYAutores_TituloConAutores() {
        Titulo titulo = mock(Titulo.class);
        Autor autorConMasTitulos = mock(Autor.class);
        Autor autorSinMasTitulos = mock(Autor.class);
        Set<Autor> autores = new HashSet<>(Arrays.asList(autorConMasTitulos, autorSinMasTitulos));

        when(titulo.getAutores()).thenReturn(autores);
        when(autorConMasTitulos.getTitulos()).thenReturn(new HashSet<>(Arrays.asList(new Titulo())));
        when(autorSinMasTitulos.getTitulos()).thenReturn(new HashSet<>());

        boolean resultado = tituloService.eliminarTituloYAutores(titulo);

        assertTrue(resultado);
        verify(autorDAO).delete(autorSinMasTitulos);
        verify(autorDAO).save(autorConMasTitulos);
        verify(tituloDAO).delete(titulo);
    }

    @Test
    void eliminarTituloYAutores_TituloEsLibro() {
        Libro libro = mock(Libro.class);
        when(libro.getAutores()).thenReturn(new HashSet<>());

        boolean resultado = tituloService.eliminarTituloYAutores(libro);

        assertTrue(resultado);
        verify(libroDAO).delete(libro);
    }

    @Test
    void eliminarTituloYAutores_TituloEsRevista() {
        Revista revista = mock(Revista.class);
        when(revista.getAutores()).thenReturn(new HashSet<>());

        boolean resultado = tituloService.eliminarTituloYAutores(revista);

        assertTrue(resultado);
        verify(revistaDAO).delete(revista);
    }

    @Test
    void eliminarTituloYAutores_TituloNoEsLibroNiRevista() {
        Titulo titulo = mock(Titulo.class);
        when(titulo.getAutores()).thenReturn(new HashSet<>());

        boolean resultado = tituloService.eliminarTituloYAutores(titulo);

        assertTrue(resultado);
        verify(tituloDAO).delete(titulo);
    }
    
    
    @Test
    void eliminarEjemplarConVerificaciones_EjemplarNoExistente() {
        when(ejemplarDAO.findById(ejemplarId)).thenReturn(Optional.empty());

        assertFalse(tituloService.eliminarEjemplarConVerificaciones(ejemplarId));
    }

    @Test
    void eliminarEjemplarConVerificaciones_EjemplarNoDisponible() {
        ejemplar.setDisponible(false);
        when(ejemplarDAO.findById(ejemplarId)).thenReturn(Optional.of(ejemplar));

        assertFalse(tituloService.eliminarEjemplarConVerificaciones(ejemplarId));
    }

    @Test
    void eliminarEjemplarConVerificaciones_EjemplarConPrestamoActivo() {
        ejemplar.setDisponible(true);
        when(ejemplarDAO.findById(ejemplarId)).thenReturn(Optional.of(ejemplar));
        when(prestamoDAO.existsByEjemplarAndActivo(ejemplar, true)).thenReturn(true);

        assertFalse(tituloService.eliminarEjemplarConVerificaciones(ejemplarId));
    }

    @Test
    void eliminarEjemplarConVerificaciones_EjemplarConReserva() {
        ejemplar.setDisponible(true);
        when(ejemplarDAO.findById(ejemplarId)).thenReturn(Optional.of(ejemplar));
        when(reservaDAO.existsByEjemplar(ejemplar)).thenReturn(true);

        assertFalse(tituloService.eliminarEjemplarConVerificaciones(ejemplarId));
    }

    @Test
    void eliminarEjemplarConVerificaciones_EliminacionExitosa() {
        ejemplar.setDisponible(true);
        when(ejemplarDAO.findById(ejemplarId)).thenReturn(Optional.of(ejemplar));
        when(prestamoDAO.existsByEjemplarAndActivo(ejemplar, true)).thenReturn(false);
        when(reservaDAO.existsByEjemplar(ejemplar)).thenReturn(false);

        assertTrue(tituloService.eliminarEjemplarConVerificaciones(ejemplarId));
        verify(reservaDAO).deleteByEjemplar(ejemplar);
        verify(prestamoDAO).deleteByEjemplarAndActivo(ejemplar, false);
        verify(ejemplarDAO).delete(ejemplar);
    }
    
    @Test
    void obtenerAutores_TodosAutoresExisten() {
        for (String nombre : autoresNombres) {
            when(autorDAO.findByNombre(nombre.trim())).thenReturn(Optional.of(new Autor(nombre.trim(), "Apellido", null)));
        }

        tituloService.obtenerAutores(autoresNombres, autores);

        assertEquals(3, autores.size());
        verify(autorDAO, never()).save(any(Autor.class));
    }

    @Test
    void obtenerAutores_AlgunosAutoresNoExisten() {
        when(autorDAO.findByNombre("Autor1")).thenReturn(Optional.of(new Autor("Autor1", "Apellido", null)));
        when(autorDAO.findByNombre("Autor2")).thenReturn(Optional.empty());
        when(autorDAO.findByNombre("Autor3")).thenReturn(Optional.empty());

        tituloService.obtenerAutores(autoresNombres, autores);

        assertEquals(3, autores.size());
        verify(autorDAO, times(2)).save(any(Autor.class));
    }

    @Test
    void obtenerAutores_NingunAutorExiste() {
        for (String nombre : autoresNombres) {
            when(autorDAO.findByNombre(nombre.trim())).thenReturn(Optional.empty());
        }

        tituloService.obtenerAutores(autoresNombres, autores);

        assertEquals(3, autores.size());
        verify(autorDAO, times(3)).save(any(Autor.class));
    }
    
    @Test
    void guardarTitulo_CuandoEsLibro() {
        Collection<Autor> autores = new HashSet<>();
        Titulo titulo = new Libro("Titulo Libro", "123456", autores, null, null, null);
        Libro libroGuardado = new Libro("Titulo Libro", "123456", autores, null, null, null);

        when(libroDAO.save(any(Libro.class))).thenReturn(libroGuardado);

        Titulo resultado = tituloService.guardarTitulo(autores, "libro", titulo);

        assertNotNull(resultado);
        assertTrue(resultado instanceof Libro);
        verify(libroDAO, times(1)).save(any(Libro.class));
    }
    
    @Test
    void guardarTitulo_CuandoEsRevista() {
        Collection<Autor> autores = new HashSet<>();
        Titulo titulo = new Revista("Titulo Revista", "654321", autores, null, null, null);
        Revista revistaGuardada = new Revista("Titulo Revista", "654321", autores, null, null, null);

        when(revistaDAO.save(any(Revista.class))).thenReturn(revistaGuardada);

        Titulo resultado = tituloService.guardarTitulo(autores, "revista", titulo);

        assertNotNull(resultado);
        assertTrue(resultado instanceof Revista);
        verify(revistaDAO, times(1)).save(any(Revista.class));
    }

    @Test
    void guardarTitulo_CuandoTipoNoValido() {
        Collection<Autor> autores = new HashSet<>();
        Titulo titulo = new Titulo(); // Un título genérico, sin especificar tipo

        Titulo resultado = tituloService.guardarTitulo(autores, "tipoNoValido", titulo);

        assertNull(resultado);
        verify(libroDAO, never()).save(any(Libro.class));
        verify(revistaDAO, never()).save(any(Revista.class));
    }
    

}
    

    
    



	

