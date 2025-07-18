package com.inm.tracewise.model.dto;

import java.util.List;

import lombok.Data;

@Data
public class TraceDTO {

	private String traceId;
	private List<AppDTO> apps;

	public TraceDTO(String traceId, List<AppDTO> apps) {
		this.traceId = traceId;
		this.apps = apps;
	}
}
