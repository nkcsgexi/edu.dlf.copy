package edu.dlf.refactoring.copy.snippet;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.ASTNode;

import com.google.inject.Inject;

import edu.dlf.refactoring.copy.Design.IDeclaredVariableBuilder;
import edu.dlf.refactoring.copy.Design.ISnippetDeclaredVariable;

public class DeclaredIdentifierBuilder implements IDeclaredVariableBuilder {
	
	private final Logger logger;

	@Inject
	public DeclaredIdentifierBuilder(Logger logger) {
		this.logger = logger;
	}

	@Override
	public ISnippetDeclaredVariable apply(ASTNode type, ASTNode name) {
		
		
		return null;
	}

	
}
