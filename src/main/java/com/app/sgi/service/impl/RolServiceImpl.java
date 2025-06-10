package com.app.sgi.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.sgi.model.Rol;
import com.app.sgi.repository.IRolRepository;
import com.app.sgi.service.IRolService;

@Service
public class RolServiceImpl implements IRolService {

    @Autowired
    private IRolRepository rolRepository;

    @Override
    public List<Rol> listar() {
        return rolRepository.findAllByOrderByNombreAsc();
    }
}