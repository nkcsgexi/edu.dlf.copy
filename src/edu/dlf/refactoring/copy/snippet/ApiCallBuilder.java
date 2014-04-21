package edu.dlf.refactoring.copy.snippet;

import java.util.List;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodInvocation;

import com.google.inject.Inject;

import edu.dlf.refactoring.copy.AstAnalyzer;
import edu.dlf.refactoring.copy.Design.IApiCall;
import edu.dlf.refactoring.copy.Design.IApiCallBuilder;

public class ApiCallBuilder extends AstAnalyzer implements IApiCallBuilder {

	private final Logger logger;

	@Inject
	public ApiCallBuilder(Logger logger) {
		this.logger = logger;
	}
	
	@Override
	public IApiCall apply(ASTNode methodCall) {
		ASTNode methodName = (ASTNode)methodCall.getStructuralProperty
			(MethodInvocation.NAME_PROPERTY);
		Stream<ASTNode> args = ((List<ASTNode>)methodCall.getStructuralProperty
			(MethodInvocation.ARGUMENTS_PROPERTY)).stream();
	
		return null;
	}

}
