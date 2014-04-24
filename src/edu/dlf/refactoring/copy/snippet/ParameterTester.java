package edu.dlf.refactoring.copy.snippet;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.ASTNode;

import com.google.inject.Inject;

import edu.dlf.refactoring.copy.Design.IParameterReplacableTester;

public class ParameterTester implements IParameterReplacableTester{
	
	private final Logger logger;

	@Inject
	public ParameterTester(Logger logger) {
		this.logger = logger;
	}
	
	@Override
	public boolean test(ASTNode t) {
		return t.getNodeType() == ASTNode.STRING_LITERAL ||
			t.getNodeType() == ASTNode.NUMBER_LITERAL ||
			t.getNodeType() == ASTNode.SIMPLE_NAME;
	}
}
