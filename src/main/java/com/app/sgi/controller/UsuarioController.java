package com.app.sgi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.app.sgi.model.Usuario;
import com.app.sgi.service.IExcelExportService;
import com.app.sgi.service.IRolService;
import com.app.sgi.service.IUsuarioService;
import com.app.sgi.util.LoggerUtil;

import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private IRolService rolService;

	@Autowired
	private IExcelExportService excelService;

	@GetMapping({ "", "/", "/listar" })
	public String listar(Model model) {
		model.addAttribute("usuarios", usuarioService.listar());
		return "pages/listarUsuario";
	}

	@GetMapping("/nuevo")
	public String nuevo(Model model) {
		model.addAttribute("usuario", new Usuario());
		model.addAttribute("roles", rolService.listar());
		return "pages/formUsuario";
	}

	@GetMapping("/editar/{id}")
	public String editar(@PathVariable Integer id, Model model, RedirectAttributes redirect) {
		Usuario usuario = usuarioService.buscarPorId(id);
		if (usuario != null) {
			model.addAttribute("usuario", usuario);
			model.addAttribute("roles", rolService.listar());
			return "pages/formUsuario";
		} else {
			redirect.addFlashAttribute("error", "El usuario con ID " + id + " no existe.");
			return "redirect:/usuario/listar";
		}
	}

	@PostMapping("/guardar")
	public String guardar(@ModelAttribute @Validated Usuario usuario, BindingResult result, RedirectAttributes redirect,
			Model model) {
	    LoggerUtil.logJson("Request guardar usuario", usuario);
		
		if (usuario.getIdUsuario() == null
				&& (usuario.getContrasena() == null || usuario.getContrasena().trim().isEmpty())) {
			result.rejectValue("contrasena", null, "La contraseña es obligatoria.");
		}

		if (result.hasErrors()) {
			LoggerUtil.log("Errores de validación en usuario", result.getAllErrors());
			model.addAttribute("roles", rolService.listar());
			model.addAttribute("usuario", usuario);
			return "pages/formUsuario";
		}

		try {
			usuarioService.guardar(usuario);
			redirect.addFlashAttribute("success", "Usuario guardado correctamente.");
			return "redirect:/usuario/listar";
		} catch (IllegalArgumentException e) {
			LoggerUtil.log("Error al guardar usuario (validación)", e.getMessage());
			model.addAttribute("error", e.getMessage());
		} catch (Exception e) {
			LoggerUtil.log("Error general al guardar usuario", e.getMessage());
			model.addAttribute("error", "Error al guardar el usuario.");
		}

		model.addAttribute("usuario", usuario);
		model.addAttribute("roles", rolService.listar());
		return "pages/formUsuario";
	}

	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable Integer id, RedirectAttributes redirect) {
		try {
			boolean status = usuarioService.eliminar(id);
			
			if(status) {
				redirect.addFlashAttribute("success", "Usuario con ID " + id + " eliminado correctamente.");
			}else {
				redirect.addFlashAttribute("error", "Usuario con ID " + id + " no se pudo eliminar.");
			}
			
		} catch (IllegalArgumentException e) {
			LoggerUtil.log("Error al eliminar usuario (validación)", e.getMessage());
			redirect.addFlashAttribute("error", e.getMessage());
		} catch (Exception e) {
			LoggerUtil.log("Error general al eliminar usuario", e.getMessage());
			redirect.addFlashAttribute("error", "Ocurrió un error al eliminar el usuario.");
		}
		return "redirect:/usuario/listar";
	}

	@GetMapping("/exportar")
	public void exportar(HttpServletResponse response) {
		try {
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename=usuarios.xlsx");

			ByteArrayInputStream stream = excelService.exportarUsuarios(usuarioService.listar());
			stream.transferTo(response.getOutputStream());

			response.flushBuffer();
		} catch (Exception e) {
			LoggerUtil.log("Error al exportar usuarios", e.getMessage());
			throw new RuntimeException("Error al exportar usuarios a Excel: " + e.getMessage(), e);
		}
	}
}
