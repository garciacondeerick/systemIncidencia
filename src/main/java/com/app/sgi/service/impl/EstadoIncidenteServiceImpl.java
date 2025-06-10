package com.app.sgi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.sgi.model.EstadoIncidente;
import com.app.sgi.repository.IEstadoIncidenteRepository;
import com.app.sgi.service.IEstadoIncidenteService;

@Service
public class EstadoIncidenteServiceImpl implements IEstadoIncidenteService{

	@Autowired
	private IEstadoIncidenteRepository estadoIncidenteRepository;
	
	@Override
	public List<EstadoIncidente> listar() {
		return estadoIncidenteRepository.findAll();
	}

	@Override
	public EstadoIncidente buscarPorId(Integer id) {
		return estadoIncidenteRepository.findById(id).orElse(null);
	}

}
