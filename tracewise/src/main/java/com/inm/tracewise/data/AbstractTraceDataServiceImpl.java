package com.inm.tracewise.data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.inm.tracewise.model.dto.AppDTO;
import com.inm.tracewise.model.dto.ClassDTO;
import com.inm.tracewise.model.dto.Trace;
import com.inm.tracewise.model.dto.TraceDTO;

public abstract class AbstractTraceDataServiceImpl implements ITraceDataService {

	protected TraceDTO convertToJson(List<Trace> results) {
		// Group by traceId
		Map<String, List<Trace>> traceGroups = results.stream().collect(Collectors.groupingBy(Trace::getTraceId));

		List<TraceDTO> traces = traceGroups.entrySet().stream().map(traceResultsEntry -> {
			String traceId = traceResultsEntry.getKey();
			List<Trace> traceList = traceResultsEntry.getValue();

			// Group by app
			Map<String, List<Trace>> appGroup = traceList.stream().collect(Collectors.groupingBy(Trace::getAppName));

			List<AppDTO> apps = appGroup.entrySet().stream().map(appEntry -> {
				String app = appEntry.getKey();
				List<Trace> appResults = appEntry.getValue();

				// Group by classname
				Map<String, List<Trace>> classGroup = appResults.stream()
						.collect(Collectors.groupingBy(Trace::getClassName));

				List<ClassDTO> classes = classGroup.entrySet().stream().map(classEntry -> {
					String classname = classEntry.getKey();
					List<String> methods = classEntry.getValue().stream().map(r -> r.getMethodName())
							.collect(Collectors.toList());
					return new ClassDTO(classname, methods);
				}).collect(Collectors.toList());

				return new AppDTO(app, classes);
			}).collect(Collectors.toList());

			return new TraceDTO(traceId, apps);
		}).collect(Collectors.toList());
		if (traces != null && !traces.isEmpty()) {
			return traces.get(0);
		}
		return null;
	}

}
