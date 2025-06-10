package com.app.sgi.service;

import java.util.List;

import com.app.sgi.model.BitacoraEstado;
import com.app.sgi.model.Incidente;

public interface IBitacoraEstadoService {
	public List<BitacoraEstado> listarPorIncidente(Incidente incidente);
}
