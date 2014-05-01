package edu.dlf.refactoring.copy;

import java.io.IOException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

import edu.dlf.refactoring.copy.Design.IApiCallBuilder;
import edu.dlf.refactoring.copy.Design.IApiParameterBuilder;
import edu.dlf.refactoring.copy.Design.ICodeSnippetBuilder;
import edu.dlf.refactoring.copy.Design.IDeclaredVariableBuilder;
import edu.dlf.refactoring.copy.Design.IParameterReplacableTester;
import edu.dlf.refactoring.copy.Design.ISearchable;
import edu.dlf.refactoring.copy.jar.BinaryClassesRepository;
import edu.dlf.refactoring.copy.snippet.ApiCallBuilder;
import edu.dlf.refactoring.copy.snippet.ApiParameterBuilder;
import edu.dlf.refactoring.copy.snippet.CodeSnippetBuilder;
import edu.dlf.refactoring.copy.snippet.ParameterTester;
import edu.dlf.refactoring.copy.snippet.VariableBuilder;

public class ServiceLocator extends AbstractModule{

	private final static String PATTERN = "%d [%p|%c|%C{1}] %m%n";
	private final static String desktop = "/home/xige/Desktop/";
	private final static AbstractModule _instance = new ServiceLocator();
	private final static Injector injector = Guice.createInjector(_instance);
	
	@Override
	protected void configure() {
		bind(ICodeSnippetBuilder.class).to(CodeSnippetBuilder.class).in(Singleton.class);
		bind(IApiCallBuilder.class).to(ApiCallBuilder.class).in(Singleton.class);
		bind(IApiParameterBuilder.class).to(ApiParameterBuilder.class).in(Singleton.class);
		bind(IDeclaredVariableBuilder.class).to(VariableBuilder.class).in(Singleton.class);
		bind(IParameterReplacableTester.class).to(ParameterTester.class).in(Singleton.class);
		bind(ISearchable.class).annotatedWith(Names.named("binary")).to(BinaryClassesRepository.class);
		bindConstant().annotatedWith(Names.named("jar")).to("/home/xige/workspace/edu.dlf.refactoring.copy/lib/");
	}
	
	@Provides
	@Singleton
	private Logger GetLogger() throws Exception {
		Logger.getRootLogger().getLoggerRepository().resetConfiguration();
		Logger.getRootLogger().addAppender(createConsoleAppender());
		Logger.getRootLogger().addAppender(createConsoleLogFileAppender());
		return Logger.getRootLogger();
	}

	private ConsoleAppender createConsoleAppender() {
		ConsoleAppender console = new ConsoleAppender();
		console.setLayout(new PatternLayout(PATTERN));
		console.setThreshold(Level.INFO);
		console.activateOptions();
		return console;
	}

	private RollingFileAppender createConsoleLogFileAppender() throws IOException {
		RollingFileAppender fa = new RollingFileAppender();
		fa.setImmediateFlush(true);
		fa.setMaximumFileSize(Integer.MAX_VALUE);
		fa.setName("ConsoleFileLog");	
		fa.setFile(desktop + "console.log", true, true, 1);
		fa.setLayout(new PatternLayout(PATTERN));
		fa.setThreshold(Level.INFO);
		fa.setAppend(true);
		fa.activateOptions();
		return fa;
	}
	
	public static <T> T ResolveType(Class T) {
		return (T) injector.getInstance(T);
	}
	
	public static <T> T ResolveType(Class T, String name) {
		return (T)injector.getInstance(Key.get(T, Names.named(name)));
	}
}
