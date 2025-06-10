package com.app.sgi.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;

import com.app.sgi.model.Usuario;
import com.app.sgi.service.IExcelExportService;
import com.app.sgi.util.ExcelUtils;

@Service
public class ExcelExportServiceImpl implements IExcelExportService {

	@Override
	public ByteArrayInputStream exportarUsuarios(List<Usuario> lista) throws Exception {
		String[] columnas = { "ID", "Nombres", "Apellidos", "Telefono", "Correo", "Estado", "Rol" };

		Workbook workbook = new XSSFWorkbook();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		Sheet hoja = workbook.createSheet("Usuarios");

		Row filaTitulo = hoja.createRow(0);
		Cell cellTitulo = filaTitulo.createCell(0);
		cellTitulo.setCellValue("Listado de Usuarios");

		cellTitulo.setCellStyle(ExcelUtils.crearEstiloTitulo(workbook, IndexedColors.BLACK));

		hoja.addMergedRegion(new CellRangeAddress(0, 0, 0, columnas.length - 1));

		XSSFColor colorFondoCabecera = new XSSFColor(new byte[] { (byte) 0, (byte) 51, (byte) 153 }); // Azul oscuro

		CellStyle estiloCabecera = ExcelUtils.crearEstiloCabecera(workbook, colorFondoCabecera);

		ExcelUtils.crearFilaCabecera(hoja, columnas, estiloCabecera);

		CellStyle estiloContenido = ExcelUtils.crearEstiloContenido(workbook);

		int filaIndex = 2;
		for (Usuario usuario : lista) {
			Row fila = hoja.createRow(filaIndex++);
			fila.createCell(0).setCellValue(usuario.getIdUsuario());
			fila.createCell(1).setCellValue(usuario.getNombres());
			fila.createCell(2).setCellValue(usuario.getApellidos());
			fila.createCell(3).setCellValue(usuario.getTelefono());
			fila.createCell(4).setCellValue(usuario.getCorreo());
			fila.createCell(5).setCellValue(usuario.getEstado() == 1 ? "Activo" : "Inactivo");
			fila.createCell(6).setCellValue(usuario.getRol().getNombre());
		}

		ExcelUtils.ajustarAnchoColumnasDinamico(hoja, 2, lista.size() + 1, columnas.length);

		workbook.write(stream);
		workbook.close();

		return new ByteArrayInputStream(stream.toByteArray());
	}
}
