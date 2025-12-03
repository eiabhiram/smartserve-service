package com.smartops.smartserve.mapper;

import java.util.List;

import org.springframework.util.StringUtils;

import jakarta.persistence.AttributeConverter;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

public class SSCommonListMapper implements AttributeConverter<List<String>, String> {

	@Override
	public String convertToDatabaseColumn(List<String> attribute) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.writeValueAsString(attribute);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<String> convertToEntityAttribute(String dbData) {
		List<String> list = null;
		if (StringUtils.hasText(dbData)) {
			TypeReference<List<String>> listType = new TypeReference<List<String>>() {
			};
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				list = objectMapper.readValue(dbData, listType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
}
