package es.uclm.GestiBiblioteca;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.uclm.GestiBiblioteca.business.controller.GestorUsuarios;
import es.uclm.GestiBiblioteca.business.entities.Usuario;
import es.uclm.GestiBiblioteca.persistence.UsuarioDAO;

@ExtendWith(MockitoExtension.class)
public class GestorUsuariosTest {

    @Mock
    private UsuarioDAO usuarioDAO;

    @InjectMocks
    private GestorUsuarios gestorUsuarios;

    private Usuario usuarioDePruebas;

    @BeforeEach
    void setUp() {
        // Configurar el usuario de pruebas con propiedades específicas
        usuarioDePruebas = new Usuario();
        usuarioDePruebas.setId(1L); // Configurar el ID según tus necesidades
        usuarioDePruebas.setNombre("Nombre de pruebas"); // Configurar el nombre según tus necesidades
        
        
    }

    @Test
    void mostrarFormularioRegistroUsuarioTest() {
        Model model = mock(Model.class);

        String result = gestorUsuarios.mostrarFormularioRegistroUsuario(model);

        assertEquals("registroUsuario", result);
        verify(model).addAttribute(eq("nuevoUsuario"), any(Usuario.class));
    }

    @Test
    void registrarUsuarioExitosoTest() {
        Usuario nuevoUsuario = new Usuario();
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
       
        when(usuarioDAO.save(any(Usuario.class))).thenReturn(null);
        when(redirectAttributes.addFlashAttribute(any(String.class), any(String.class))).thenReturn(null);
        String result = gestorUsuarios.registrarUsuario(nuevoUsuario, redirectAttributes);

        assertEquals("redirect:/resultadoRegistroUsuario", result);
        verify(usuarioDAO).save(nuevoUsuario);
        verify(redirectAttributes).addFlashAttribute("mensajeExito", "Usuario registrado exitosamente.");
        verifyNoMoreInteractions(redirectAttributes, usuarioDAO);
    }

    @Test
    void registrarUsuarioConErrorTest() {
        Usuario nuevoUsuario = new Usuario();
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        doThrow(new RuntimeException("Simulated error")).when(usuarioDAO).save(any(Usuario.class));

        String result = gestorUsuarios.registrarUsuario(nuevoUsuario, redirectAttributes);

        assertEquals("redirect:/resultadoRegistroUsuario", result);
        verify(usuarioDAO).save(nuevoUsuario);
        verify(redirectAttributes).addFlashAttribute("mensajeError", "Error al registrar el usuario.");
        verifyNoMoreInteractions(redirectAttributes, usuarioDAO);
    }

    @Test
    void mostrarResultadoRegistroTest() {
        String result = gestorUsuarios.mostrarResultadoRegistro();
        assertEquals("resultadoRegistroUsuario", result);
    }
}
