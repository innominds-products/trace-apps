package com.inm.tracewise.agent;

import org.slf4j.MDC;

public class MicrometerContextUtil {
	private static final ThreadLocal<String> trace = new ThreadLocal<>();
	private static final String traceParameterName = "X-TraceWise-Id";

	public static String getTraceId() {
		if (MDC.getMDCAdapter() != null) {
			String traceId = MDC.get(traceParameterName);
			return traceId != null ? traceId : trace.get();
		}
		return null;
	}

	public static void setTraceId(String traceId) {
		trace.set(traceId);
	}

	public static void clearTraceId() {
		trace.remove();
	}
}
