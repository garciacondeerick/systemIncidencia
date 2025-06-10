package com.app.sgi.service;

import java.io.ByteArrayInputStream;
import java.util.List;
import com.app.sgi.model.Usuario;

public interface IExcelExportService {
	public ByteArrayInputStream exportarUsuarios(List<Usuario> lista) throws Exception;
}
