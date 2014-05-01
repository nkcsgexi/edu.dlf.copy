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
import edu.dlf.refactoring.copy.Design.IApiParameter;
import edu.dlf.refactoring.copy.Design.IApiParameterBuilder;

public class ApiCallBuilder extends AstAnalyzer implements IApiCallBuilder {

	private final Logger logger;
	private final IApiParameterBuilder builder;

	@Inject
	public ApiCallBuilder(Logger logger, IApiParameterBuilder builder) {
		this.logger = logger;
		this.builder = builder;
	}
	
	@Override
	public IApiCall apply(ASTNode methodCall) {
		final ASTNode methodName = (ASTNode)methodCall.getStructuralProperty
			(MethodInvocation.NAME_PROPERTY);
		final Stream<IApiParameter> arguments = ((List<ASTNode>)methodCall.
			getStructuralProperty(MethodInvocation.ARGUMENTS_PROPERTY)).
				stream().map(builder);
		return new IApiCall() {
			@Override
			public Stream<IApiParameter> getAllParameters() {
				return arguments;
			}
			@Override
			public String getName() {
				return null;
			}};
	}
}
