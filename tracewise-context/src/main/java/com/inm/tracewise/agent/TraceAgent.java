package com.inm.tracewise.agent;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.jar.JarFile;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.inm.tracewise.agent.instrument.TraceCallable;
import com.inm.tracewise.agent.instrument.TraceRunnable;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.matcher.ElementMatcher.Junction;
import net.bytebuddy.matcher.ElementMatchers;

public class TraceAgent {

	private static final String agentJarPath = "C:/Users/sdesha/.m2/repository/com/inm/tracewise-context/1.0-SNAPSHOT/tracewise-context-1.0-SNAPSHOT.jar";

	public static final Logger log = Logger.getLogger(TraceAgent.class.getName());

	public static void premain(String agentArgs, Instrumentation inst) throws IOException {
		configureAgentLog();
//		log.info("[TraceAgent] Starting instrumentation...");
//		attachAgentJarToBootstrapClassLoader(inst);
		prepareAgentBuilder(inst);
		setupMicrometer();
	}

	private static void configureAgentLog() {
		createLogDir();
		initializeLog();
	}

	private static void initializeLog() {
		try (InputStream configStream = TraceAgent.class.getClassLoader().getResourceAsStream("logging.properties")) {
			if (configStream != null) {
				LogManager.getLogManager().readConfiguration(configStream);
			} else {
				System.err.println("logging.properties not found in agent JAR");
			}
		} catch (Exception e) {
			System.err.println("Failed to load logging config: " + e.getMessage());
		}
	}

	private static void createLogDir() {
		Properties props = new Properties();
		try (InputStream configStream = TraceAgent.class.getClassLoader().getResourceAsStream("logging.properties")) {
			if (configStream != null) {
				props.load(configStream);
				// Get log file path
				String logFilePath = props.getProperty("java.util.logging.FileHandler.pattern");
				if (logFilePath == null || logFilePath.isBlank()) {
					throw new IllegalArgumentException("java.util.logging.FileHandler.pattern property is missing");
				}

				// Create parent directories
				Path logPath = Paths.get(logFilePath);
				Path parentDir = logPath.getParent();
				if (parentDir != null && !Files.exists(parentDir)) {
					Files.createDirectories(parentDir);
				}
			} else {
				System.err.println("logging.properties not found in trace-wise agent JAR");
			}
		} catch (Exception e) {
			System.err.println("Failed to load logging config: " + e.getMessage());
		}
	}

	private static void setupMicrometer() {
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
//		.with(new InjectionStrategy.UsingInstrumentation(inst, new File(agentJarPath)))
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

	private static void attachAgentJarToBootstrapClassLoader(Instrumentation inst) throws RuntimeException {
		try {
			// Load helper JAR into bootstrap class loader
			File jar = new File(agentJarPath);
			inst.appendToBootstrapClassLoaderSearch(new JarFile(jar));
			System.out.println("Attaching to Bootstrap loader is done");
		} catch (IOException e) {
			throw new RuntimeException("Failed to load bootstrap JAR", e);
		}
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
