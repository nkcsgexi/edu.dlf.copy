package edu.dlf.refactoring.copy.snippet;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

public class DeclaredIdentifierBuilder {
	
	private final Logger logger;

	@Inject
	public DeclaredIdentifierBuilder(Logger logger) {
		this.logger = logger;
	}

	
}
