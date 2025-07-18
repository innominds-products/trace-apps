package com.inm.tracewise.data.impl.rdbms;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.inm.tracewise.data.AbstractTraceDataServiceImpl;
import com.inm.tracewise.data.ITraceDataService;
import com.inm.tracewise.model.dto.Trace;
import com.inm.tracewise.model.dto.TraceDTO;
import com.inm.tracewise.model.dto.TraceInfo;
import com.inm.tracewise.repository.TraceRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@ConditionalOnProperty(name = "tracewise.data", havingValue = "rdbms")
public class RdbmsTraceDataServiceImpl extends AbstractTraceDataServiceImpl implements ITraceDataService {

	@Autowired
	private TraceInfo traceInfo;

	private final TraceRepository traceRepo;

	RdbmsTraceDataServiceImpl(TraceRepository traceRepo) {
		this.traceRepo = traceRepo;
	}

	@Override
	public TraceDTO findTraces(String traceId) {
		// Get data from db
		List<Trace> traceList = traceRepo.findByTraceId(traceId);
		return convertToJson(traceList);
	}

	@Override
	public Collection fetchClasses(String className) {
		return this.traceInfo.getClassMap().get(className);
	}

	@Override
	public void save(Trace trace) {
		this.traceRepo.save(trace);
	}

}
