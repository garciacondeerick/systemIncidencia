package com.app.sgi;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import com.app.sgi.model.*;
import com.app.sgi.repository.*;
import com.app.sgi.service.impl.IncidenteServiceImpl;
import com.app.sgi.util.ConstantesApp;

import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class IncidenteServiceImplTest {

    @InjectMocks
    private IncidenteServiceImpl incidenteService;

    @Mock
    private IIncidenteRepository incidenteRepository;

    @Mock
    private IUsuarioRepository usuarioRepository;

    @Mock
    private IEstadoIncidenteRepository estadoRepository;

    @Mock
    private IBitacoraEstadoRepository bitacoraRepository;

    @Mock
    private HttpSession session;

    private Usuario cliente;
    private Usuario tecnico;
    private EstadoIncidente estado;
    private Incidente incidente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Rol rolCliente = new Rol();
        rolCliente.setIdRol(ConstantesApp.ID_ROLE_CLIENTE);
        
        Rol rolTecnico = new Rol();
        rolTecnico.setIdRol(ConstantesApp.ID_ROLE_TECNICO);
        
        cliente = new Usuario();
        cliente.setIdUsuario(1);
        cliente.setCorreo("cliente@demo.com");
        cliente.setRol(rolCliente);

        tecnico = new Usuario();
        tecnico.setIdUsuario(2);
        tecnico.setCorreo("tecnico@demo.com");
        tecnico.setRol(rolTecnico);

        estado = new EstadoIncidente();
        estado.setIdEstado(ConstantesApp.ID_ESTADO_PENDIENTE);
        estado.setNombre("PENDIENTE");

        incidente = new Incidente();
        incidente.setIdIncidente(10);
        incidente.setTitulo("Error ATM");
        incidente.setCliente(cliente);
        incidente.setDescripcion("No funciona");
        incidente.setPrioridad("Alta");
    }

    @Test
    void testGuardarIncidenteOk() {
        when(usuarioRepository.findById(cliente.getIdUsuario())).thenReturn(Optional.of(cliente));
        when(estadoRepository.findById(ConstantesApp.ID_ESTADO_PENDIENTE)).thenReturn(Optional.of(estado));
        when(incidenteRepository.save(any())).thenReturn(incidente);

        Incidente result = incidenteService.guardar(incidente);

        assertNotNull(result);
        assertEquals("Error ATM", result.getTitulo());
        verify(bitacoraRepository, times(1)).save(any(BitacoraEstado.class));
    }

    @Test
    void testGuardarIncidenteClienteNoExiste() {
        incidente.setCliente(new Usuario());
        incidente.getCliente().setIdUsuario(999);

        when(usuarioRepository.findById(999)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            incidenteService.guardar(incidente);
        });

        assertEquals("Cliente no encontrado.", exception.getMessage());
    }

    @Test
    void testAsignarTecnicoOk() {
        EstadoIncidente estadoProceso = new EstadoIncidente(ConstantesApp.ID_ESTADO_EN_PROCESO, "EN PROCESO");

        when(incidenteRepository.findById(10)).thenReturn(Optional.of(incidente));
        when(usuarioRepository.findById(2)).thenReturn(Optional.of(tecnico));
        when(estadoRepository.findById(ConstantesApp.ID_ESTADO_EN_PROCESO)).thenReturn(Optional.of(estadoProceso));

        incidenteService.asignarTecnico(10, 2, cliente);

        verify(incidenteRepository, times(1)).save(incidente);
        verify(bitacoraRepository, times(1)).save(any(BitacoraEstado.class));
        assertEquals("EN PROCESO", incidente.getEstado().getNombre());
    }

    @Test
    void testListarPorRolAdmin() {
        Usuario admin = new Usuario();
        admin.setIdUsuario(99);
        admin.setRol(new Rol(ConstantesApp.ID_ROLE_ADMIN, ConstantesApp.ROLE_ADMIN));

        when(session.getAttribute("usuarioSesion")).thenReturn(admin);
        when(incidenteRepository.findAll()).thenReturn(List.of(incidente));

        List<Incidente> lista = incidenteService.listarPorRol(session);
        assertEquals(1, lista.size());
    }

    @Test
    void testListarSinAsignar() {
        when(incidenteRepository.findByTecnicoIsNull()).thenReturn(List.of(incidente));

        List<Incidente> sinAsignar = incidenteService.listarSinAsignar();
        assertFalse(sinAsignar.isEmpty());
    }
}