package com.app.sgi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.app.sgi.model.Rol;

@Repository
public interface IRolRepository extends JpaRepository<Rol, Integer> {
	public List<Rol> findAllByOrderByNombreAsc();
}