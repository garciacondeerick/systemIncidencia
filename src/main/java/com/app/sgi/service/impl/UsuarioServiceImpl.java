package com.app.sgi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.sgi.model.Usuario;
import com.app.sgi.repository.IUsuarioRepository;
import com.app.sgi.service.IUsuarioService;
import com.app.sgi.util.ConstantesApp;

@Service
public class UsuarioServiceImpl implements IUsuarioService {
	@Autowired
	private IUsuarioRepository usuarioRepository;
	
	@Autowired
	private PasswordEncoder encoder;

	@Override
	public List<Usuario> listar() {
		return usuarioRepository.findAll();
	}

	@Override
	public Usuario buscarPorId(Integer id) {
		return usuarioRepository.findById(id).orElse(null);
	}

	@Override
	public Usuario guardar(Usuario usuario) {
		int count = usuarioRepository.existeCorreo(usuario.getCorreo().trim(),
				usuario.getIdUsuario() != null ? usuario.getIdUsuario() : 0);

		if (count > 0) {
			throw new IllegalArgumentException("El correo "+usuario.getCorreo().trim()+" ya está registrado, intente con otro.");
		}

		if (usuario.getIdUsuario() == null) {
	        usuario.setContrasena(encoder.encode(usuario.getContrasena()));
	    } else {
	        Usuario existente = usuarioRepository.findById(usuario.getIdUsuario())
	                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + usuario.getIdUsuario()));
	        usuario.setContrasena(existente.getContrasena());
	    }
		
		return usuarioRepository.save(usuario);
	}

	@Override
	public boolean eliminar(Integer id) {
		if (usuarioRepository.existsById(id)) {
	        usuarioRepository.deleteById(id);
	        return true;
	    }
	    return false;
	}
	
	@Override
	public Usuario buscarPorCorreo(String correo) {
	    return usuarioRepository.findByCorreo(correo)
	        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con correo: " + correo));
	}

	@Override
	public List<Usuario> listarTecnicosActivos() {
		return usuarioRepository.listarPorRolActivos(ConstantesApp.ROLE_TECNICO);
	}
}
