package com.app.sgi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String mostrarLogin() {
        return "pages/login"; 
    }
    
    @GetMapping("/acceso-denegado")
    public String accesoDenegado() {
        return "pages/accesoDenegado";
    }
}