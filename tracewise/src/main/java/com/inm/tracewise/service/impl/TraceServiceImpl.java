package com.inm.tracewise.service.impl;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.inm.tracewise.data.ITraceDataService;
import com.inm.tracewise.model.dto.TraceDTO;
import com.inm.tracewise.service.ITraceService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TraceServiceImpl implements ITraceService {

	private ITraceDataService dataService;

	public TraceServiceImpl(ITraceDataService dataService) {
		this.dataService = dataService;
	}

	@Override
	public TraceDTO fetchTraces(String traceId) {
		return this.dataService.findTraces(traceId);
	}

	@Override
	public Collection fetchClasses(String className) {
		return this.dataService.fetchClasses(className);
	}

}
