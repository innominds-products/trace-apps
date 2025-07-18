package com.inm.tracewise.model.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "Trace")
public class Trace {

	@Column(name = "trace_id")
	private String traceId;

	@Column(name = "span_id")
	private String spanId;

	@Column(name = "class_name")
	private String className;

	@Column(name = "app_name")
	private String appName;

	@Column(name = "method_name")
	private String methodName;

	public Trace() {

	}

	public Trace(String traceId, String spanId, String className, String appName, String methodName) {
		super();
		this.traceId = traceId;
		this.spanId = spanId;
		this.className = className;
		this.appName = appName;
		this.methodName = methodName;
	}
}