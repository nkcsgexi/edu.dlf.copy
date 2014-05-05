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
import edu.dlf.refactoring.copy.Design.IExpressionUpdatedNodesCollecter;
import edu.dlf.refactoring.copy.Design.IUpdatedNodesContainer;

public class ApiCallBuilder extends AstAnalyzer implements IApiCallBuilder {

	private final Logger logger;
	private final IApiParameterBuilder builder;
	private final IExpressionUpdatedNodesCollecter expAnalyzer;

	@Inject
	public ApiCallBuilder(Logger logger, IApiParameterBuilder builder, 
			IExpressionUpdatedNodesCollecter expAnalyzer) {
		this.logger = logger;
		this.builder = builder;
		this.expAnalyzer = expAnalyzer;
	}
	
	@Override
	public IApiCall apply(ASTNode methodCall) {
		final ASTNode methodName = (ASTNode)methodCall.getStructuralProperty
			(MethodInvocation.NAME_PROPERTY);
		final Stream<IApiParameter> arguments = ((List<ASTNode>)methodCall.
			getStructuralProperty(MethodInvocation.ARGUMENTS_PROPERTY)).
				stream().map(builder);
		final ASTNode express = (ASTNode)methodCall.getStructuralProperty
			(MethodInvocation.EXPRESSION_PROPERTY);
		final Stream<ASTNode> otherNodes = express == null ? Stream.empty() : this.
			expAnalyzer.apply(express);
		
		return new IApiCall() {
			@Override
			public Stream<IApiParameter> getAllParameters() {
				return arguments;
			}
			@Override
			public Stream<ASTNode> get() {
				return Stream.concat(otherNodes, getAllParameters().flatMap
					(IUpdatedNodesContainer::get)).distinct();
			}};
	}
}
