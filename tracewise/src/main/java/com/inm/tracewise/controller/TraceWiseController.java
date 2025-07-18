package com.inm.tracewise.controller;

import java.util.Collection;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inm.tracewise.model.dto.TraceDTO;
import com.inm.tracewise.service.ITraceService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/v1/traces")
@Slf4j
public class TraceWiseController {

	private final ITraceService traceService;

	public TraceWiseController(ITraceService traceService) {
		this.traceService = traceService;
	}

	@GetMapping("/traceInfo/{traceId}")
	public ResponseEntity<TraceDTO> placeOrder(@PathVariable("traceId") String traceId) {
		TraceDTO traces = traceService.fetchTraces(traceId);
		return ResponseEntity.ok(traces);
	}

	@GetMapping("/classInfo/{className}")
	public ResponseEntity<Collection> findClassTraces(@PathVariable("className") String className) {
		Collection traces = traceService.fetchClasses(className);
		return ResponseEntity.ok(traces);
	}

}
