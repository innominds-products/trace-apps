package com.inm.tracewise.agent.instrument;

import com.inm.tracewise.agent.MicrometerContextUtil;

public class TraceRunnable implements Runnable {
	private final Runnable delegate;
	private final String traceId;

	public TraceRunnable(Runnable delegate) {
		this.delegate = delegate;
		this.traceId = MicrometerContextUtil.getTraceId();
	}

	@Override
	public void run() {
		MicrometerContextUtil.setTraceId(traceId);
		try {
			delegate.run();
		} finally {
			MicrometerContextUtil.clearTraceId();
		}
	}
}