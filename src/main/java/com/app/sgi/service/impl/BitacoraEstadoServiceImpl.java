package com.app.sgi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.sgi.model.BitacoraEstado;
import com.app.sgi.model.Incidente;
import com.app.sgi.repository.IBitacoraEstadoRepository;
import com.app.sgi.service.IBitacoraEstadoService;

@Service
public class BitacoraEstadoServiceImpl implements IBitacoraEstadoService {

    @Autowired
    private IBitacoraEstadoRepository bitacoraRepository;

    @Override
    public List<BitacoraEstado> listarPorIncidente(Incidente incidente) {
        return bitacoraRepository.findByIncidenteOrderByFechaAsc(incidente);
    }
}