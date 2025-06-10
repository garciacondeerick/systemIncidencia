package com.app.sgi.service;

import java.util.List;

import com.app.sgi.model.EstadoIncidente;

public interface IEstadoIncidenteService {
	public List<EstadoIncidente> listar();
	public EstadoIncidente buscarPorId(Integer id);
}
