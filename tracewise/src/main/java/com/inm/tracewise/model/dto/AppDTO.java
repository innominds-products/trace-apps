package com.inm.tracewise.model.dto;

import java.util.List;

import lombok.Data;

@Data
public class AppDTO {

	private String app;
	private List<ClassDTO> classes;

	public AppDTO(String app, List<ClassDTO> classes) {
		this.app = app;
		this.classes = classes;
	}
}
