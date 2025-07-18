package com.inm.tracewise.agent;

import java.lang.reflect.Method;
import java.util.logging.Logger;

import net.bytebuddy.asm.Advice;

public class TraceAdvice {

	public static final Logger log = Logger.getLogger(TraceAdvice.class.getName());

	@Advice.OnMethodEnter
	public static void onEnter(@Advice.Origin Method method) {
		String traceId = MicrometerContextUtil.getTraceId();
		String logMsg = String.format("[TRACE-BEGIN]%s::%s.%s[TRACE-END]", traceId,
				method.getDeclaringClass().getName(), method.getName());
		log.info(logMsg);
	}

}
