package com.app.sgi.service;

import java.util.List;

import com.app.sgi.model.Usuario;

public interface IUsuarioService {
	public List<Usuario> listar();
	public Usuario buscarPorId(Integer id);
	public Usuario guardar(Usuario usuario);
	public boolean eliminar(Integer id);
	public Usuario buscarPorCorreo(String correo);
	public List<Usuario> listarTecnicosActivos();
}
