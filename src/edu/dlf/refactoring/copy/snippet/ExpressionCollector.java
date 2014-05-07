package edu.dlf.refactoring.copy.snippet;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldAccess;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import edu.dlf.refactoring.copy.Design.IIntegrationInforCollector;
import edu.dlf.refactoring.copy.Design.IIntegrationInforContainer;

public class ExpressionCollector extends AbstractIntegrationInforCollector{

	private final Logger logger;
	private final IIntegrationInforCollector callBuilder;

	@Inject
	public ExpressionCollector(
			Logger logger, 
			@Named("invocation") IIntegrationInforCollector callBuilder) {
		this.logger = logger;
		this.callBuilder = callBuilder;
	}
	
	@Override
	public IIntegrationInforContainer apply(ASTNode expression) {
		if(expression == null)
			return createInforContainer(null, null);
		ASTNode superExp;
		switch(expression.getNodeType()) {
		case ASTNode.SIMPLE_NAME:
			return createInforContainer(null, expression);
		case ASTNode.QUALIFIED_NAME: 
			return createInforContainer(expression, null);
		case ASTNode.FIELD_ACCESS:
			superExp = (ASTNode) expression.getStructuralProperty
				(FieldAccess.EXPRESSION_PROPERTY);
			return apply(superExp);
		case ASTNode.METHOD_INVOCATION:
			return this.callBuilder.apply(expression);
		case ASTNode.STRING_LITERAL:
			return createInforContainer(null, expression);
		case ASTNode.NUMBER_LITERAL:
			return createInforContainer(null, expression);
		}
		return createInforContainer(null, null);
	}
}
