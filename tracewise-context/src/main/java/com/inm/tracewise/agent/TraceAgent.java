package com.inm.tracewise.agent;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.concurrent.Callable;

import com.inm.tracewise.agent.instrument.TraceCallable;
import com.inm.tracewise.agent.instrument.TraceRunnable;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.matcher.ElementMatcher.Junction;
import net.bytebuddy.matcher.ElementMatchers;

public class TraceAgent {

//	private static final Logger log = LoggerFactory.getLogger(TraceAgent.class);

	public static void premain(String agentArgs, Instrumentation inst) throws IOException {
//		log.info("[TraceAgent] Starting instrumentation...");
		prepareAgentBuilder(inst);
		setupMicrometer();
	}

	private static void setupMicrometer() {
//		MicrometerContextUtil.initializeObservation();
		setMicrometerTracingProperties();
	}

	private static void setMicrometerTracingProperties() {
		System.setProperty("management.tracing.enabled", "true");
		System.setProperty("management.tracing.sampling.probability", "${TRACING_SAMPLING_PROBABILITY:1.0}");
		System.setProperty("spring.kafka.template.observation-enabled", "true");
		System.setProperty("spring.kafka.listener.observation-enabled", "true");
		System.setProperty("management.tracing.baggage.remote-fields", "X-TraceWise-Id");
		System.setProperty("management.tracing.baggage.correlation.fields", "X-TraceWise-Id");
		System.setProperty("management.tracing.enabled", "true");
		System.setProperty("management.tracing.enabled", "true");
		System.setProperty("management.tracing.enabled", "true");
	}

	private static void prepareAgentBuilder(Instrumentation inst) throws IOException {
		String targetAppPackagePrefix = System.getProperty("package-prefix");
		if (targetAppPackagePrefix == null || "".equals(targetAppPackagePrefix.trim())) {
			throw new RuntimeException("Please provide jvm argument via property/parameter name package-prefix");
		}
		System.out.println("Package Prefix :: " + targetAppPackagePrefix);

		Junction<NamedElement> targetAppMatcher = findTargetPackages(targetAppPackagePrefix);
		new AgentBuilder.Default()
		// To debug byte buddy
//				.with(AgentBuilder.Listener.StreamWriting.toSystemOut())
				.ignore(ElementMatchers.nameStartsWith("net.bytebuddy.")
						.or(ElementMatchers.nameStartsWith("com.inm.tracewise."))
						.or(ElementMatchers.nameStartsWith("io.micrometer.")))
//				.type(packageMatcher)
				.type(targetAppMatcher).transform((builder, typeDescription, classLoader, module, protectionDomain) -> {
					// Optional: inspect protectionDomain for filtering
//					if (protectionDomain != null && protectionDomain.getCodeSource() != null) {
//						String location = protectionDomain.getCodeSource().getLocation().toString();
//						System.out.println("Transforming class from: " + location);
//					}
					Builder<?> agentBuilder = setMethodAdvisor(builder);
					return agentBuilder;
				}).installOn(inst);
	}

	private static Junction<NamedElement> findTargetPackages(String targetAppPackagePrefix) {
		Junction<NamedElement> targetAppMatcher = null;
		String[] packages = targetAppPackagePrefix.split(",");
		for (String pack : packages) {
			if (targetAppMatcher == null) {
				targetAppMatcher = ElementMatchers.nameStartsWith(pack);
			} else {
				targetAppMatcher.or(ElementMatchers.nameStartsWith(pack));
			}
		}
		ElementMatchers.nameStartsWith(targetAppPackagePrefix);
		return targetAppMatcher;
	}

	private static Builder<?> setMethodAdvisor(Builder<?> builder) {
		Builder<?> agentBuilder = builder.visit(Advice.to(TraceAdvice.class).on(ElementMatchers.isMethod()))
				.method(ElementMatchers.named("submit").and(ElementMatchers.takesArgument(0, Runnable.class)))
				.intercept(Advice.to(SubmitRunnableAdvice.class))
				.method(ElementMatchers.named("submit").and(ElementMatchers.takesArgument(0, Callable.class)))
				.intercept(Advice.to(SubmitCallableAdvice.class))
				.method(ElementMatchers.named("execute").and(ElementMatchers.takesArgument(0, Runnable.class)))
				.intercept(Advice.to(SubmitRunnableAdvice.class));
		return agentBuilder;
	}

	public static class SubmitRunnableAdvice {
		@Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
		static boolean wrap(@Advice.Argument(value = 0, readOnly = false) Runnable task) {
			if (!(task instanceof TraceRunnable)) {
				task = new TraceRunnable(task);
			}
			return false;
		}
	}

	public static class SubmitCallableAdvice {
		@Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
		static boolean wrap(@Advice.Argument(value = 0, readOnly = false) Callable<?> task) {
			if (!(task instanceof TraceCallable)) {
				task = new TraceCallable<>(task);
			}
			return false;
		}
	}
}
