package com.inm.tracewise.model.dto;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class TraceInfo {

	private Map<String, Set<Trace>> traceMap = new ConcurrentHashMap<>();
	private Map<String, Set<String>> classMap = new ConcurrentHashMap<>();

}
