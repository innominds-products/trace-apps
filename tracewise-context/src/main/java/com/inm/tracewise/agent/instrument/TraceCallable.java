package com.inm.tracewise.agent.instrument;

import java.util.concurrent.Callable;

import com.inm.tracewise.agent.MicrometerContextUtil;

public class TraceCallable<V> implements Callable<V> {
	private final Callable<V> delegate;
	private final String traceId;

	public TraceCallable(Callable<V> delegate) {
		this.delegate = delegate;
		this.traceId = MicrometerContextUtil.getTraceId();
	}

	@Override
	public V call() throws Exception {
		MicrometerContextUtil.setTraceId(traceId);
		try {
			return delegate.call();
		} finally {
			MicrometerContextUtil.clearTraceId();
		}
	}
}