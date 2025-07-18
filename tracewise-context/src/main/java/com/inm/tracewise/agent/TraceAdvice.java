package com.inm.tracewise.agent;

import java.lang.reflect.Method;

import net.bytebuddy.asm.Advice;

public class TraceAdvice {

//	private static final Logger log = LoggerFactory.getLogger(TraceAdvice.class);

	@Advice.OnMethodEnter
	public static void onEnter(@Advice.Origin Method method) {
		String traceId = MicrometerContextUtil.getTraceId();
		System.out.printf("[TRACE-BEGIN] %s :: %s.%s [TRACE-END]%n", traceId, method.getDeclaringClass().getName(),
				method.getName());
	}

}
