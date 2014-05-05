package edu.dlf.refactoring.copy.snippet;

import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.ASTNode;

import com.google.inject.Inject;

import edu.dlf.refactoring.copy.Design.IApiParameter;
import edu.dlf.refactoring.copy.Design.IApiParameterBuilder;
import edu.dlf.refactoring.copy.Design.IExpressionUpdatedNodesCollecter;

public class ApiParameterBuilder implements IApiParameterBuilder{
	
	private final Logger logger;
	private final IExpressionUpdatedNodesCollecter expAnalyzer;

	@Inject
	public ApiParameterBuilder(Logger logger, IExpressionUpdatedNodesCollecter 
			expAnalyzer) {
		this.logger = logger;
		this.expAnalyzer = expAnalyzer;
	}
	
	@Override
	public IApiParameter apply(final ASTNode parameter) {
		return new IApiParameter() {
			@Override
			public Stream<ASTNode> get() {
				return expAnalyzer.apply(parameter).distinct();
		}};
	}
}
