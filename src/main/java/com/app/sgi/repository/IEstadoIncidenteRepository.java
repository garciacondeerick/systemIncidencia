package com.app.sgi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.app.sgi.model.EstadoIncidente;

@Repository
public interface IEstadoIncidenteRepository extends JpaRepository<EstadoIncidente, Integer> {
}