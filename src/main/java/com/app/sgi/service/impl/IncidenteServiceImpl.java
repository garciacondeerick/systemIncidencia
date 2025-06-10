package com.app.sgi.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.sgi.model.BitacoraEstado;
import com.app.sgi.model.EstadoIncidente;
import com.app.sgi.model.Incidente;
import com.app.sgi.model.Usuario;
import com.app.sgi.repository.IBitacoraEstadoRepository;
import com.app.sgi.repository.IEstadoIncidenteRepository;
import com.app.sgi.repository.IIncidenteRepository;
import com.app.sgi.repository.IUsuarioRepository;
import com.app.sgi.service.IIncidenteService;
import com.app.sgi.util.ConstantesApp;

import jakarta.servlet.http.HttpSession;

import org.springframework.transaction.annotation.Transactional;

@Service
public class IncidenteServiceImpl implements IIncidenteService {

    @Autowired
    private IIncidenteRepository incidenteRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;
    
    @Autowired
    private IEstadoIncidenteRepository estadoIncidenteRepository;
    
    @Autowired
    private IBitacoraEstadoRepository bitacoraEstadoRepository;

    @Override
    public List<Incidente> listar() {
        return incidenteRepository.findAll();
    }

    @Override
    @Transactional
    public Incidente guardar(Incidente incidente) {
        if (incidente.getCliente() == null || incidente.getCliente().getIdUsuario() == null) {
            throw new IllegalArgumentException("Debe seleccionar un cliente válido.");
        }

        Usuario cliente = usuarioRepository.findById(incidente.getCliente().getIdUsuario())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado."));

        EstadoIncidente estadoIncidente = estadoIncidenteRepository.findById(ConstantesApp.ID_ESTADO_PENDIENTE)
                .orElseThrow(() -> new IllegalArgumentException("Estado incidente no encontrado."));

        incidente.setEstado(estadoIncidente);
        incidente.setCliente(cliente);
        incidente.setFechaCreacion(LocalDateTime.now());

        Incidente incidenteGuardado = incidenteRepository.save(incidente);

        BitacoraEstado bitacora = new BitacoraEstado();
        bitacora.setIncidente(incidenteGuardado);
        bitacora.setEstado(estadoIncidente);
        bitacora.setUsuario(cliente); 
        bitacora.setFecha(LocalDateTime.now());

        bitacoraEstadoRepository.save(bitacora);

        return incidenteGuardado;
    }

    @Override
    public Incidente buscarPorId(Integer id) {
        return incidenteRepository.findById(id).orElse(null);
    }

    @Override
    public boolean eliminar(Integer id) {
        if (incidenteRepository.existsById(id)) {
            incidenteRepository.deleteById(id);
            return true;
        }
        return false;
    }

	@Override
	public Map<String, Object> obtenerResumenPorCorreo(String correo) {
		Map<String, Object> datos = new HashMap<>();
	    
	    Usuario usuario = usuarioRepository.findByCorreo(correo)
	        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

	    boolean isAdmin = usuario.getRol().getNombre().equalsIgnoreCase(ConstantesApp.ROLE_ADMIN);
	    boolean isTecnico = usuario.getRol().getNombre().equalsIgnoreCase(ConstantesApp.ROLE_TECNICO);

	    int id = usuario.getIdUsuario();
	    long pendientes, enProceso, resueltos;

	    if (isTecnico) {
	        pendientes = incidenteRepository.contarPorEstadoYTecnico(1, id);
	        enProceso = incidenteRepository.contarPorEstadoYTecnico(2, id);
	        resueltos = incidenteRepository.contarPorEstadoYTecnico(3, id);
	    } else {
	        pendientes = incidenteRepository.contarPorEstadoYCliente(1, id, isAdmin);
	        enProceso = incidenteRepository.contarPorEstadoYCliente(2, id, isAdmin);
	        resueltos = incidenteRepository.contarPorEstadoYCliente(3, id, isAdmin);
	    }

	    datos.put("usuario", usuario);
	    datos.put("rol", usuario.getRol().getNombre());
	    datos.put("pendientes", pendientes);
	    datos.put("enProceso", enProceso);
	    datos.put("resueltos", resueltos);
	    datos.put("total", pendientes + enProceso + resueltos);

	    return datos;
	}

	@Override
	public List<Incidente> listarSinAsignar() {
		return incidenteRepository.findByTecnicoIsNull();
	}
	
	@Override
	@Transactional
	public void asignarTecnico(Integer incidenteId, Integer tecnicoId, Usuario usuarioLogeado) {
	    try {
	        Incidente incidente = incidenteRepository.findById(incidenteId)
	            .orElseThrow(() -> new IllegalArgumentException("Incidente no encontrado."));

	        if (tecnicoId == null) {
	            throw new IllegalArgumentException("Debe seleccionar un técnico.");
	        }

	        Usuario tecnico = usuarioRepository.findById(tecnicoId)
	            .orElseThrow(() -> new IllegalArgumentException("Técnico no encontrado."));

	        EstadoIncidente estadoEnProceso = estadoIncidenteRepository.findById(ConstantesApp.ID_ESTADO_EN_PROCESO)
	            .orElseThrow(() -> new IllegalArgumentException("Estado en proceso no encontrado."));

	        incidente.setTecnico(tecnico);
	        incidente.setEstado(estadoEnProceso);
	        incidenteRepository.save(incidente);

	        BitacoraEstado bitacora = new BitacoraEstado();
	        bitacora.setIncidente(incidente);
	        bitacora.setEstado(estadoEnProceso);
	        bitacora.setUsuario(usuarioLogeado);
	        bitacora.setFecha(LocalDateTime.now());

	        bitacoraEstadoRepository.save(bitacora);

	    } catch (Exception e) {
	        throw e;
	    }
	}

	@Override
	public List<Incidente> listarPorRol(HttpSession session) {
		Usuario usuarioSesion = (Usuario) session.getAttribute("usuarioSesion");

	    if (usuarioSesion == null || usuarioSesion.getRol() == null) {
	        return List.of(); 
	    }
	    
	    String nombreRol = usuarioSesion.getRol().getNombre();

	    if (nombreRol.equalsIgnoreCase(ConstantesApp.ROLE_ADMIN)) {
	    	System.out.println("ADMIN: "+ usuarioSesion.getIdUsuario());
	    	return incidenteRepository.findAll();
	    } else if (nombreRol.equalsIgnoreCase(ConstantesApp.ROLE_CLIENTE)) {
	    	System.out.println("CLIENTE: "+ usuarioSesion.getIdUsuario());
	    	return incidenteRepository.findByClienteIdUsuario(usuarioSesion.getIdUsuario());
	    } else if (nombreRol.equalsIgnoreCase(ConstantesApp.ROLE_TECNICO)) {
	    	System.out.println("TECNICO: "+ usuarioSesion.getIdUsuario());
	    	  return incidenteRepository.findByTecnicoIdUsuario(usuarioSesion.getIdUsuario());
	    }

	    return List.of();
	}
}