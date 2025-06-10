package com.app.sgi.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;

public class ExcelUtils {

	public static CellStyle crearEstiloTitulo(Workbook workbook, IndexedColors color) {
		CellStyle estiloTitulo = workbook.createCellStyle();
		estiloTitulo.setAlignment(HorizontalAlignment.CENTER);
		estiloTitulo.setVerticalAlignment(VerticalAlignment.CENTER);

		Font fuenteTitulo = workbook.createFont();
		fuenteTitulo.setBold(true);
		fuenteTitulo.setFontHeightInPoints((short) 16);

		fuenteTitulo.setColor(color.getIndex());
		estiloTitulo.setFont(fuenteTitulo);

		return estiloTitulo;
	}

	public static CellStyle crearEstiloCabecera(Workbook workbook, XSSFColor colorFondo) {
		CellStyle estiloCabecera = workbook.createCellStyle();

		estiloCabecera.setFillForegroundColor(colorFondo);
		estiloCabecera.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		estiloCabecera.setBorderBottom(BorderStyle.THIN);
		estiloCabecera.setBorderTop(BorderStyle.THIN);
		estiloCabecera.setBorderLeft(BorderStyle.THIN);
		estiloCabecera.setBorderRight(BorderStyle.THIN);

		estiloCabecera.setAlignment(HorizontalAlignment.CENTER);
		estiloCabecera.setVerticalAlignment(VerticalAlignment.CENTER);

		Font fuenteCabecera = workbook.createFont();
		fuenteCabecera.setBold(true);
		fuenteCabecera.setColor(IndexedColors.WHITE.getIndex());
		estiloCabecera.setFont(fuenteCabecera);

		return estiloCabecera;
	}

	public static CellStyle crearEstiloContenido(Workbook workbook) {
		CellStyle estiloContenido = workbook.createCellStyle();
		estiloContenido.setBorderBottom(BorderStyle.THIN);
		estiloContenido.setBorderTop(BorderStyle.THIN);
		estiloContenido.setBorderLeft(BorderStyle.THIN);
		estiloContenido.setBorderRight(BorderStyle.THIN);
		estiloContenido.setAlignment(HorizontalAlignment.LEFT);
		estiloContenido.setVerticalAlignment(VerticalAlignment.CENTER);

		return estiloContenido;
	}

	public static CellStyle crearEstiloFondoColor(Workbook workbook, IndexedColors color) {
		CellStyle estilo = workbook.createCellStyle();
		estilo.setFillForegroundColor(color.getIndex());
		estilo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		estilo.setBorderBottom(BorderStyle.THIN);
		estilo.setBorderTop(BorderStyle.THIN);
		estilo.setBorderLeft(BorderStyle.THIN);
		estilo.setBorderRight(BorderStyle.THIN);
		estilo.setAlignment(HorizontalAlignment.CENTER);
		estilo.setVerticalAlignment(VerticalAlignment.CENTER);

		Font fuente = workbook.createFont();
		fuente.setBold(true);
		fuente.setColor(IndexedColors.WHITE.getIndex());
		estilo.setFont(fuente);

		return estilo;
	}

	public static void ajustarAnchoColumnasDinamico(Sheet hoja, int startRow, int endRow, int numCols) {
		for (int colIndex = 0; colIndex < numCols; colIndex++) {
			int maxLength = 0;
			for (int rowIndex = startRow; rowIndex <= endRow; rowIndex++) {
				Row row = hoja.getRow(rowIndex);
				if (row != null && row.getCell(colIndex) != null) {
					String cellValue = row.getCell(colIndex).toString();
					maxLength = Math.max(maxLength, cellValue.length());
				}
			}

			hoja.setColumnWidth(colIndex, 256 * Math.min(maxLength + 2, 50));// Limitar tamaño maximo
		}
	}

	public static void ajustarAnchoColumnas(Sheet hoja, String[] columnas) {
		for (int i = 0; i < columnas.length; i++) {
			hoja.autoSizeColumn(i);
		}
	}

	public static void crearFilaCabecera(Sheet hoja, String[] columnas, CellStyle estiloCabecera) {
		Row filaCabecera = hoja.createRow(1); 
		for (int i = 0; i < columnas.length; i++) {
			Cell cell = filaCabecera.createCell(i);
			cell.setCellValue(columnas[i]);
			cell.setCellStyle(estiloCabecera);
		}
	}
}