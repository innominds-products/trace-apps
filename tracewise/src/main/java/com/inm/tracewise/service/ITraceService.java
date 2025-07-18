package com.inm.tracewise.service;

import java.util.Collection;

import com.inm.tracewise.model.dto.TraceDTO;

public interface ITraceService {

	TraceDTO fetchTraces(String traceId);

	Collection fetchClasses(String className);

}
