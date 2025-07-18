package com.inm.tracewise.model.dto;

import java.util.List;

import lombok.Data;

@Data
public class ClassDTO {

	private String classname;
	private List<String> methods;

	public ClassDTO(String classname, List<String> methods) {
		this.classname = classname;
		this.methods = methods;
	}

}
