package es.uclm.GestiBiblioteca;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.qos.logback.core.model.Model;
import es.uclm.GestiBiblioteca.business.controller.GestorTitulos;
import es.uclm.GestiBiblioteca.business.entities.Autor;
import es.uclm.GestiBiblioteca.business.entities.Ejemplar;
import es.uclm.GestiBiblioteca.business.entities.Titulo;
import es.uclm.GestiBiblioteca.persistence.EjemplarDAO;
import es.uclm.GestiBiblioteca.persistence.TituloDAO;
import es.uclm.GestiBiblioteca.services.TituloService;

@ExtendWith(MockitoExtension.class)
public class GestorTitulosTest {

    @Mock
    private TituloService tituloService;

    @Mock
    private EjemplarDAO ejemplarDAO;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private GestorTitulos gestorTitulos;

    @Mock
    private TituloDAO tituloDAO;

    @Mock
    private org.springframework.ui.Model model;

    
    

    @SuppressWarnings("unchecked")
    @Test
    void altaTituloSubmitTest() {
        // Configuración de datos de prueba
        Titulo titulo = new Titulo();
        titulo.setIsbn("123456789");
        titulo.setTitulo_obra("Título de prueba");

        String tipoTitulo = "libro";

        List<String> autoresSeleccionados = Arrays.asList("Autor1", "Autor2");

        // Crear instancia de Autor para cada nombre de autor
        Autor autor1 = new Autor();
        autor1.setNombre("Autor1");

        Autor autor2 = new Autor();
        autor2.setNombre("Autor2");

        // Configurar el comportamiento de los mocks
        Collection<Autor> autores = new HashSet<>(Arrays.asList(autor1, autor2));
        doAnswer(invocation -> {
            Collection<Autor> autoresDestino = (Collection<Autor>) invocation.getArgument(1);
            autoresDestino.addAll(autores);
            return null;
        }).when(tituloService).obtenerAutores(any(String[].class), any(Collection.class));

        Titulo savedTitulo = new Titulo();
        savedTitulo.setIsbn("123456789");
        savedTitulo.setTitulo_obra("Título de prueba");
        when(tituloService.guardarTitulo(eq(autores), eq(tipoTitulo), eq(titulo))).thenReturn(savedTitulo);

        // Llamada al método bajo prueba
        String result = gestorTitulos.altaTituloSubmit(titulo, tipoTitulo, autoresSeleccionados, redirectAttributes);

        // Verificaciones
        assertEquals("redirect:/resultAlta", result);

        // Verificar las interacciones
        verify(tituloService).obtenerAutores(any(String[].class), anySet());
        verify(tituloService).guardarTitulo(eq(autores), eq(tipoTitulo), eq(titulo));
        verify(ejemplarDAO).save(any(Ejemplar.class));

        // Verificar que se agregaron los atributos de redirección
        verify(redirectAttributes).addFlashAttribute("mensajeExito", "Título agregado exitosamente.");
        verify(redirectAttributes).addFlashAttribute("titulo", savedTitulo);
        verify(redirectAttributes).addFlashAttribute("tipoTitulo", tipoTitulo.equalsIgnoreCase("libro") ? "Libro" : "Revista");
        verify(redirectAttributes).addFlashAttribute("autores", autores.stream().map(Autor::getNombre).collect(Collectors.toList()));
    }
    
    @SuppressWarnings("unchecked")
    @Test
    void altaTituloSubmitTestError() {
        // Configuración de datos de prueba
        Titulo titulo = new Titulo();
        titulo.setIsbn("123456789");
        titulo.setTitulo_obra("Título de prueba");

        String tipoTitulo = "libro";

        List<String> autoresSeleccionados = Arrays.asList("Autor1", "Autor2");

        // Crear instancia de Autor para cada nombre de autor
        Autor autor1 = new Autor();
        autor1.setNombre("Autor1");

        Autor autor2 = new Autor();
        autor2.setNombre("Autor2");

        // Configurar el comportamiento de los mocks
        Collection<Autor> autores = new HashSet<>(Arrays.asList(autor1, autor2));
        doAnswer(invocation -> {
            Collection<Autor> autoresDestino = (Collection<Autor>) invocation.getArgument(1);
            autoresDestino.addAll(autores);
            return null;
        }).when(tituloService).obtenerAutores(any(String[].class), any(Collection.class));

        Titulo savedTitulo = new Titulo();
        savedTitulo.setIsbn("123456789");
        savedTitulo.setTitulo_obra("Título de prueba");
        when(tituloService.guardarTitulo(eq(autores), eq(tipoTitulo), eq(titulo))).thenReturn(null);

        // Llamada al método bajo prueba
        String result = gestorTitulos.altaTituloSubmit(titulo, tipoTitulo, autoresSeleccionados, redirectAttributes);

        // Verificaciones
        assertEquals("Error", result);

        // Verificar las interacciones
        verify(tituloService).obtenerAutores(any(String[].class), anySet());
        verify(tituloService).guardarTitulo(eq(autores), eq(tipoTitulo), eq(titulo));
    }        
    @Test
    void actualizarTituloSubmitTest() {
        // Configuración de datos de prueba
        Titulo titulo = new Titulo();
        titulo.setId(1L);
        titulo.setIsbn("123456789");
        titulo.setTitulo_obra("Título de prueba");

        Titulo existingTitulo = new Titulo();
        existingTitulo.setId(1L);
        existingTitulo.setIsbn("987654321");
        existingTitulo.setTitulo_obra("Título existente");

        // Configurar el comportamiento del mock
        when(tituloDAO.findById(1L)).thenReturn(Optional.of(existingTitulo));
        when(tituloDAO.save(any(Titulo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Llamada al método bajo prueba
        String result = gestorTitulos.actualizarTituloSubmit(titulo,model,redirectAttributes);

        // Verificaciones
        assertEquals("redirect:/resultadoActualizacion", result);

        // Verificar las interacciones
        verify(tituloDAO).findById(1L);
        verify(tituloDAO).save(existingTitulo);

        // Verificar que se agregaron los atributos de redirección
        verify(redirectAttributes).addFlashAttribute("result2", "Título actualizado exitosamente.");
        verify(redirectAttributes).addFlashAttribute("titulo", existingTitulo);

        // Verificar que se agregaron los atributos al modelo
        verify(model).addAttribute("titulo", existingTitulo);
        verify(model).addAttribute("result2", "Título actualizado exitosamente.");
    }
    @Test
    void actualizarTituloSubmitTestNotFound() {
        // Configuración de datos de prueba
        Titulo titulo = new Titulo();
        titulo.setId(1L);
        titulo.setIsbn("123456789");
        titulo.setTitulo_obra("Título de prueba");


        // Configurar el comportamiento del mock
        when(tituloDAO.findById(1L)).thenReturn(Optional.empty());
   

        // Llamada al método bajo prueba
        String result = gestorTitulos.actualizarTituloSubmit(titulo,model,redirectAttributes);

        // Verificaciones
        assertEquals("redirect:/resultadoActualizacion", result);

        // Verificar las interacciones
        verify(tituloDAO).findById(1L);
        // Verificar que se agregaron los atributos al modelo
        redirectAttributes.addFlashAttribute("result2", "No se encontró el título para actualizar.");

    }
    @Test
    void borrarTituloTest() {
        // Configuración de datos de prueba
        String isbn = "123456789";

        Titulo titulo1 = new Titulo();
        titulo1.setId(1L);
        titulo1.setIsbn(isbn);

        Titulo titulo2 = new Titulo();
        titulo2.setId(2L);
        titulo2.setIsbn(isbn);

        List<Titulo> titulosABorrar = new ArrayList<>();
        titulosABorrar.add(titulo1);
        titulosABorrar.add(titulo2);

        // Configurar el comportamiento del mock
        when(tituloService.eliminarTituloConVerificaciones(eq(1L))).thenReturn(true);
        when(tituloService.eliminarTituloConVerificaciones(eq(2L))).thenReturn(false);
        when(tituloDAO.findByIsbn(eq(isbn))).thenReturn(titulosABorrar);

        // Llamada al método bajo prueba
        String result = gestorTitulos.borrarTitulo(isbn, redirectAttributes);

        // Verificaciones
        assertEquals("redirect:/mostrarResultadoBorrado", result);

        // Verificar las interacciones
        verify(tituloService, times(2)).eliminarTituloConVerificaciones(anyLong());

        // Verificar que se agregaron los atributos de redirección
        verify(redirectAttributes).addFlashAttribute("mensajeExito", "Título con ISBN " + isbn + " borrado exitosamente");
        verify(redirectAttributes).addFlashAttribute("mensajeError", "El título con ISBN " + isbn + " no puede ser borrado debido a ejemplares activos o reservados");
    }
    @Test
    void borrarTituloTestEmptyList() {
       
        List<Titulo> titulosABorrar = new ArrayList<>();
        String isbn ="1";

        when(tituloDAO.findByIsbn(eq(isbn))).thenReturn(titulosABorrar);

        // Llamada al método bajo prueba
        String result = gestorTitulos.borrarTitulo(isbn, redirectAttributes);

        // Verificaciones
        assertEquals("redirect:/mostrarResultadoBorrado", result);

        // Verificar que se agregaron los atributos de redirección
        verify(redirectAttributes).addFlashAttribute("mensajeError", "No se encontró ningún título con ISBN " + isbn);
    }
    @Test
    void altaEjemplarTest() {
        // Configuración de datos de prueba
        Long idTitulo = 1L;
        Titulo titulo = new Titulo();
        titulo.setId(idTitulo);
        titulo.setTitulo_obra("Título de prueba");

        Ejemplar ejemplar = new Ejemplar(titulo, true);

        // Configurar el comportamiento del mock
        when(tituloDAO.findById(eq(idTitulo))).thenReturn(Optional.of(titulo));
        when(ejemplarDAO.save(any(Ejemplar.class))).thenReturn(ejemplar);

        // Llamada al método bajo prueba
        String result = gestorTitulos.altaEjemplar(idTitulo, ejemplar, redirectAttributes);

        // Verificaciones
        assertEquals("redirect:/mostrarResultadoAltaEjemplar", result);

        // Verificar las interacciones
        verify(tituloDAO).findById(idTitulo);
        verify(ejemplarDAO).save(any(Ejemplar.class));

        // Verificar que se agregaron los atributos de redirección
        verify(redirectAttributes).addFlashAttribute("ejemplarAgregado", true);
        verify(redirectAttributes).addFlashAttribute("mensaje", "Nuevo ejemplar agregado exitosamente al título: " + titulo.getTitulo_obra());
        verify(redirectAttributes).addFlashAttribute("titulo", titulo);
    }

    @Test
    void altaEjemplarTestTituloNoEncontrado() {
        // Configuración de datos de prueba
        Long idTitulo = 1L;

        // Configurar el comportamiento del mock para devolver un Optional vacío
        when(tituloDAO.findById(eq(idTitulo))).thenReturn(Optional.empty());

        // Llamada al método bajo prueba
        String result = gestorTitulos.altaEjemplar(idTitulo, new Ejemplar(), redirectAttributes);

        // Verificaciones
        assertEquals("redirect:/mostrarResultadoAltaEjemplar", result);

        // Verificar las interacciones
        verify(tituloDAO).findById(idTitulo);

        // Verificar que se agregaron los atributos de redirección
        verify(redirectAttributes).addFlashAttribute("ejemplarAgregado", false);
        verify(redirectAttributes).addFlashAttribute("error", "El título con ID " + idTitulo + " no existe.");
    }
    @Test
    void bajaEjemplarTest() {
        // Configuración de datos de prueba
        List<Long> idsEjemplares = Arrays.asList(1L, 2L, 3L);

        // Configurar el comportamiento del mock
        when(tituloService.eliminarEjemplarConVerificaciones(anyLong())).thenReturn(true);

        // Llamada al método bajo prueba
        String result = gestorTitulos.bajaEjemplar(idsEjemplares, redirectAttributes);

        // Verificaciones
        assertEquals("redirect:/resultadoBajaEjemplar", result);

        // Verificar las interacciones
        for (Long id : idsEjemplares) {
            verify(tituloService).eliminarEjemplarConVerificaciones(id);
        }

        // Verificar que se agregaron los atributos de redirección
        verify(redirectAttributes).addFlashAttribute("mensaje", "Ejemplares dados de baja exitosamente.");
    }

    @Test
    void bajaEjemplarTestConError() {
        // Configuración de datos de prueba
        List<Long> idsEjemplares = Arrays.asList(1L, 2L, 3L);

        // Configurar el comportamiento del mock
        when(tituloService.eliminarEjemplarConVerificaciones(anyLong())).thenReturn(false);

        // Llamada al método bajo prueba
        String result = gestorTitulos.bajaEjemplar(idsEjemplares, redirectAttributes);

        // Verificaciones
        assertEquals("redirect:/resultadoBajaEjemplar", result);

        // Verificar las interacciones
        for (Long id : idsEjemplares) {
            verify(tituloService).eliminarEjemplarConVerificaciones(id);
        }

        // Verificar que se agregaron los atributos de redirección
        verify(redirectAttributes).addFlashAttribute("error", "El ejemplar con id: 1 no puede ser eliminado.");
    }
    
    @Test
    void bajaEjemplarTestListaVacia() {
        // Configuración de datos de prueba
        List<Long> idsEjemplares = Arrays.asList();

        // Llamada al método bajo prueba
        String result = gestorTitulos.bajaEjemplar(idsEjemplares, redirectAttributes);

        // Verificaciones
        assertEquals("redirect:/resultadoBajaEjemplar", result);

        // Verificar las interacciones
        for (Long id : idsEjemplares) {
            verify(tituloService).eliminarEjemplarConVerificaciones(id);
        }

        // Verificar que se agregaron los atributos de redirección
        verify(redirectAttributes).addFlashAttribute("error", "No se seleccionó ningún ejemplar.");
    }

}