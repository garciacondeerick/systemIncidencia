package com.app.sgi.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.app.sgi.security.UserDetailsImpl;
import com.app.sgi.service.IIncidenteService;

@Controller
public class HomeController {
	@Autowired
	private IIncidenteService incidenteService;
	
	@GetMapping("/home")
	public String home(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
	    String correo = userDetails.getUsername(); 
	    Map<String, Object> resumen = incidenteService.obtenerResumenPorCorreo(correo);

	    model.addAllAttributes(resumen);
	    return "pages/home";
	}
}