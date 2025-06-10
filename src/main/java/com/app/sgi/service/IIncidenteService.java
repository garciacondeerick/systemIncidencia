package com.app.sgi.service;

import java.util.List;
import java.util.Map;

import com.app.sgi.model.Incidente;
import com.app.sgi.model.Usuario;

import jakarta.servlet.http.HttpSession;

public interface IIncidenteService {
	public List<Incidente> listar();

	public Incidente guardar(Incidente incidente);

	public Incidente buscarPorId(Integer id);

	public boolean eliminar(Integer id);
	
	public Map<String, Object> obtenerResumenPorCorreo(String correo);
	
	public List<Incidente> listarSinAsignar();
	
	public void asignarTecnico(Integer incidenteId, Integer tecnicoId, Usuario usuarioLogeado);
	
	public List<Incidente> listarPorRol(HttpSession session);
}
