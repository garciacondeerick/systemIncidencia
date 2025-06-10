package com.app.sgi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.sgi.model.BitacoraEstado;
import com.app.sgi.model.Incidente;

@Repository
public interface IBitacoraEstadoRepository extends JpaRepository<BitacoraEstado, Integer> {
	  public List<BitacoraEstado> findByIncidenteOrderByFechaAsc(Incidente incidente);
}