package com.app.sgi.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.sgi.model.BitacoraEstado;
import com.app.sgi.model.Incidente;
import com.app.sgi.model.Usuario;
import com.app.sgi.service.IBitacoraEstadoService;
import com.app.sgi.service.IIncidenteService;
import com.app.sgi.service.IUsuarioService;
import com.app.sgi.util.LoggerUtil;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/incidente")
public class IncidenteController {
	
	@Autowired
	private IIncidenteService incidenteService;
	
	@Autowired
	private IBitacoraEstadoService bitacoraService;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@GetMapping("/nuevo")
	public String nuevaIncidencia(Model model) {
	    model.addAttribute("incidente", new Incidente());
	    return "pages/formNuevaIncidencia"; 
	}

	@PostMapping("/guardar")
	public String guardarIncidencia(
	        @ModelAttribute @Validated Incidente incidente,
	        BindingResult result,
	        RedirectAttributes redirect,
	        Model model) {
		LoggerUtil.logJson("Request guardar incidencia", incidente);
		
	    if (result.hasErrors()) {
	    	LoggerUtil.log("Errores de validación en incidencia", result.getAllErrors());
	        model.addAttribute("incidente", incidente);
	        return "pages/formNuevaIncidencia";
	    }

	    try {
	        String username = SecurityContextHolder.getContext().getAuthentication().getName();
	        Usuario usuarioActual = usuarioService.buscarPorCorreo(username);
	        LoggerUtil.log("Usuario autenticado", username);
	        
	        incidente.setCliente(usuarioActual);
	        incidenteService.guardar(incidente);
	        redirect.addFlashAttribute("success", "Incidencia registrada correctamente.");
	        return "redirect:/incidente/listar";

	    } catch (IllegalArgumentException e) {
	    	LoggerUtil.log("Error específico al guardar incidencia", e.getMessage());
	        model.addAttribute("error", e.getMessage());
	    } catch (Exception e) {
	    	LoggerUtil.log("Error general al guardar incidencia", e.getMessage());
	        model.addAttribute("error", "Ocurrió un error al registrar la incidencia.");
	    }

	    model.addAttribute("incidente", incidente);
	    return "pages/formNuevaIncidencia";
	}
	
	@GetMapping("/listar")
	public String listarIncidencias(Model model , HttpSession sesion) {
	    model.addAttribute("incidentes", incidenteService.listarPorRol(sesion));
	    return "pages/listarIncidencias";
	}
	
	@GetMapping("/bitacora/ver/{id}")
	@ResponseBody
	public ResponseEntity<List<BitacoraEstado>> obtenerBitacora(@PathVariable("id") Integer idIncidente) {
		LoggerUtil.log("Obteniendo bitácora para incidente", idIncidente);
		
		Incidente incidente = incidenteService.buscarPorId(idIncidente);
	    if (incidente == null) {
	        return ResponseEntity.notFound().build();
	    }
	    List<BitacoraEstado> bitacoras = bitacoraService.listarPorIncidente(incidente);
	    LoggerUtil.log("Bitácora obtenida", bitacoras.size());
	    return ResponseEntity.ok(bitacoras);
	}
	
	@GetMapping("/sin-asignar")
	public String listarSinAsignar(Model model) {
	    model.addAttribute("incidentes", incidenteService.listarSinAsignar());
	    model.addAttribute("tecnicos", usuarioService.listarTecnicosActivos());
	    return "pages/listarIncidenciasSinAsignar";
	}
	
	@GetMapping("/asignar/{id}")
	public String mostrarFormularioAsignar(@PathVariable Integer id, Model model, 
			RedirectAttributes redirect) {
	    Incidente incidente = incidenteService.buscarPorId(id);
	    
	    if (incidente == null) {
	        redirect.addFlashAttribute("error", "No se encontro incidente con ID "+ id);
	        return "redirect:/incidente/sin-asignar";
	    }
	    
	    model.addAttribute("incidente", incidente);
	    model.addAttribute("tecnicos", usuarioService.listarTecnicosActivos()); 
	    return "pages/formAsignarTecnico";
	}
	
	@PostMapping("/asignar-tecnico")
	public String asignarTecnico(Integer incidenteId, Integer tecnicoId, RedirectAttributes redirect) {
        LoggerUtil.log("Asignando técnico", "Incidente ID: " + incidenteId + " | Técnico ID: " + tecnicoId);

		if (tecnicoId == null) {
	        redirect.addFlashAttribute("error", "Debe seleccionar un técnico.");
	        return "redirect:/incidente/asignar/" + incidenteId;
	    }

	    try {
	        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
	        Usuario usuarioLogeado = usuarioService.buscarPorCorreo(correo);

	        incidenteService.asignarTecnico(incidenteId, tecnicoId, usuarioLogeado);
	        redirect.addFlashAttribute("success", "Técnico asignado correctamente.");
	        return "redirect:/incidente/sin-asignar";
	    } catch (IllegalArgumentException e) {
	    	LoggerUtil.log("Error al asignar técnico (validación)", e.getMessage());
	        redirect.addFlashAttribute("error", e.getMessage());
	    } catch (Exception e) {
	    	LoggerUtil.log("Error general al asignar técnico", e.getMessage());
	        redirect.addFlashAttribute("error", "Error al asignar el técnico.");
	       
	    }
	    return "redirect:/incidente/asignar/" + incidenteId;
	}
}
