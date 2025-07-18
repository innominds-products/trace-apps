package com.inm.tracewise.data.impl.memory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.inm.tracewise.data.AbstractTraceDataServiceImpl;
import com.inm.tracewise.data.ITraceDataService;
import com.inm.tracewise.model.dto.Trace;
import com.inm.tracewise.model.dto.TraceDTO;
import com.inm.tracewise.model.dto.TraceInfo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@ConditionalOnProperty(name = "tracewise.data", havingValue = "inmemory")
public class InMemoryTraceDataServiceImpl extends AbstractTraceDataServiceImpl implements ITraceDataService {

	@Autowired
	private TraceInfo traceInfo;

	@Override
	public TraceDTO findTraces(String traceId) {
		List<Trace> traceList = new ArrayList<Trace>(this.traceInfo.getTraceMap().get(traceId));
		return convertToJson(traceList);
	}

	@Override
	public Collection fetchClasses(String className) {
		return this.traceInfo.getClassMap().get(className);
	}

	@Override
	public void save(Trace trace) {
		String traceId = trace.getTraceId();
		Set<Trace> traces = this.traceInfo.getTraceMap().get(traceId);
		if (traces == null) {
			traces = new LinkedHashSet<>();
			this.traceInfo.getTraceMap().put(traceId, traces);
		}
		traces.add(trace);
	}

}
