package com.app.sgi.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class LoggerUtil {
	private static final Logger logger = LoggerFactory.getLogger(LoggerUtil.class);
	private static final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

	public static void log(String texto, Object valor) {
		try {
			logger.info("{}: {}", texto, valor);
		} catch (Exception e) {
			logger.error("Error al loguear texto y valor", e);
		}
	}

	public static void logJson(String titulo, Object objeto) {
		try {
			String json = objectMapper.writeValueAsString(objeto);
			logger.info("{}:\n{}", titulo, json);
		} catch (Exception e) {
			logger.error("Error al convertir el objeto a JSON para el título: {}", titulo, e);
		}
	}
}
