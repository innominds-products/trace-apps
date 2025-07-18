package com.inm.tracewise.data;

import java.util.Collection;

import com.inm.tracewise.model.dto.Trace;
import com.inm.tracewise.model.dto.TraceDTO;

public interface ITraceDataService {

	TraceDTO findTraces(String traceId);

	Collection fetchClasses(String className);

	void save(Trace trace);

}
