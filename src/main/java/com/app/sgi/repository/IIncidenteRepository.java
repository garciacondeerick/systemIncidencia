package com.app.sgi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.app.sgi.model.Incidente;

@Repository
public interface IIncidenteRepository extends JpaRepository<Incidente, Integer> {
    // Admin o Cliente
    @Query("SELECT COUNT(i) FROM Incidente i WHERE (i.estado.idEstado = :estadoId) AND (:isAdmin = true OR i.cliente.idUsuario = :userId)")
    public long contarPorEstadoYCliente(@Param("estadoId") int estadoId, @Param("userId") int userId, @Param("isAdmin") boolean isAdmin);

    // Técnico
    @Query("SELECT COUNT(i) FROM Incidente i WHERE (i.estado.idEstado = :estadoId) AND i.tecnico.idUsuario = :userId")
    public long contarPorEstadoYTecnico(@Param("estadoId") int estadoId, @Param("userId") int userId);

    public List<Incidente> findByTecnicoIsNull();
    
    public List<Incidente> findByTecnicoIdUsuario(int idUsuario);
    public List<Incidente> findByClienteIdUsuario(int idUsuario);
}