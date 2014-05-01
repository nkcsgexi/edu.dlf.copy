package edu.dlf.refactoring.copy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

public abstract class FileAnalyzer {
	
	private final Logger logger;

	protected FileAnalyzer(Logger logger) {
		this.logger = logger;
	}
	
	protected Stream<Path> getAllFilePaths(String directory) {
		try{
			ArrayList<Path> paths = new ArrayList<Path>();
			Files.walk(Paths.get(directory)).forEach(filePath -> {
			    if (Files.isRegularFile(filePath)) {
			       paths.add(filePath.toAbsolutePath());
			    }
			});
			return paths.stream();
		}catch(Exception e) {
			logger.fatal(e);
			return Stream.empty();
		}
	}

}
