package edu.dlf.refactoring.copy.snippet;

import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldAccess;

import com.google.inject.Inject;

import edu.dlf.refactoring.copy.AstAnalyzer;
import edu.dlf.refactoring.copy.Design.IApiCallBuilder;
import edu.dlf.refactoring.copy.Design.IExpressionUpdatedNodesCollecter;

public class ExpressionAnalyzer extends AstAnalyzer implements 
	IExpressionUpdatedNodesCollecter{

	private final Logger logger;
	private final IApiCallBuilder callBuilder;

	@Inject
	public ExpressionAnalyzer(Logger logger, IApiCallBuilder callBuilder) {
		this.logger = logger;
		this.callBuilder = callBuilder;
	}
	
	@Override
	public Stream<ASTNode> apply(ASTNode expression) {
		ASTNode superExp;
		switch(expression.getNodeType()) {
		case ASTNode.SIMPLE_NAME:
			return Stream.of(expression);
		case ASTNode.QUALIFIED_NAME: 
			break;
		case ASTNode.FIELD_ACCESS:
			superExp = (ASTNode) expression.getStructuralProperty
				(FieldAccess.EXPRESSION_PROPERTY);
			return apply(superExp).distinct();
		case ASTNode.METHOD_INVOCATION:
			return this.callBuilder.apply(expression).get();
		}
		return Stream.empty();
	}
}
