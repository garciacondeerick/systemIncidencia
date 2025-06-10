package com.app.sgi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.app.sgi.model.Usuario;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {
	public Optional<Usuario> findByCorreo(String correo);
	
	@Query("SELECT COUNT(u) FROM Usuario u "
			+ " WHERE u.correo = :correo AND (:id = 0 OR u.idUsuario != :id)")
	public int existeCorreo(@Param("correo") String correo, @Param("id") Integer id);
	
	@Query("SELECT u FROM Usuario u WHERE u.rol.nombre = :nombreRol AND u.estado = 1")
	public List<Usuario> listarPorRolActivos(@Param("nombreRol") String nombreRol);
}