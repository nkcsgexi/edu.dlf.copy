package edu.dlf.refactoring.copy;

import java.io.IOException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class ServiceLocator extends AbstractModule{

	private static String PATTERN = "%d [%p|%c|%C{1}] %m%n";
	private static String desktop = "/home/xige/Desktop/";
	
	@Override
	protected void configure() {
		
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

}
