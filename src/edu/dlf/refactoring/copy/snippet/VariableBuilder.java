package edu.dlf.refactoring.copy.snippet;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.ASTNode;

import com.google.inject.Inject;

import edu.dlf.refactoring.copy.Design.IDeclaredVariableBuilder;
import edu.dlf.refactoring.copy.Design.ISnippetDeclaredVariable;

public class VariableBuilder implements IDeclaredVariableBuilder{

	private final Logger logger;

	@Inject
	public VariableBuilder(Logger logger) {
		this.logger = logger;
	}
	
	@Override
	public ISnippetDeclaredVariable apply(final ASTNode type, final ASTNode 
			name) {
		return new ISnippetDeclaredVariable() {
			@Override
			public String getType() {
				return type.toString();
			}
			@Override
			public String getIdentifier() {
				return name.toString();
		}};
	}

}
