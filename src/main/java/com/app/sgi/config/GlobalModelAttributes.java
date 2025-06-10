package com.app.sgi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.app.sgi.model.Usuario;
import com.app.sgi.service.IUsuarioService;
import com.app.sgi.util.ConstantesApp;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalModelAttributes {
	@Autowired
	private IUsuarioService usuarioService;

	@ModelAttribute
	public void addRequestUri(Model model, HttpServletRequest request) {
		model.addAttribute("requestURI", request.getRequestURI());
	}

	@ModelAttribute
	public void addGlobalAttributes(Model model, HttpServletRequest request, HttpSession session) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return;
        }

        Usuario usuarioSesion = (Usuario) session.getAttribute("usuarioSesion");

        if (usuarioSesion == null) {
            String correo = authentication.getName();
            usuarioSesion = usuarioService.buscarPorCorreo(correo);
            session.setAttribute("usuarioSesion", usuarioSesion);
        }

        model.addAttribute("usuarioSesion", usuarioSesion);

        if (usuarioSesion != null && usuarioSesion.getRol() != null) {
            String nombreRol = usuarioSesion.getRol().getNombre();
            model.addAttribute("esAdmin", nombreRol.equalsIgnoreCase(ConstantesApp.ROLE_ADMIN));
            model.addAttribute("esTecnico", nombreRol.equalsIgnoreCase(ConstantesApp.ROLE_TECNICO));
            model.addAttribute("esCliente", nombreRol.equalsIgnoreCase(ConstantesApp.ROLE_CLIENTE));
        }
	}
}