package edu.dlf.refactoring.copy.snippet;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodInvocation;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import edu.dlf.refactoring.copy.Design.IIntegrationInforCollector;
import edu.dlf.refactoring.copy.Design.IIntegrationInforContainer;
import edu.dlf.refactoring.copy.Design.IMergable;

public class ApiCallCollector extends AbstractIntegrationInforCollector {

	private final Logger logger;
	private final IIntegrationInforCollector argumentAnalyzer;
	private final IIntegrationInforCollector expressionAnalyzer;
	
	@Inject
	public ApiCallCollector(
			Logger logger, 
			@Named("parameter") IIntegrationInforCollector builder,
			@Named("expression") IIntegrationInforCollector expAnalyzer) {
		this.logger = logger;
		this.argumentAnalyzer = builder;
		this.expressionAnalyzer = expAnalyzer;
	}
	
	@Override
	public IIntegrationInforContainer apply(ASTNode methodCall) {
		final ASTNode methodName = (ASTNode)methodCall.getStructuralProperty
			(MethodInvocation.NAME_PROPERTY);
		final List<IIntegrationInforContainer> arguments = ((List<ASTNode>)methodCall.
			getStructuralProperty(MethodInvocation.ARGUMENTS_PROPERTY)).
				stream().map(argumentAnalyzer).collect(Collectors.toList());
		final ASTNode express = (ASTNode)methodCall.getStructuralProperty
			(MethodInvocation.EXPRESSION_PROPERTY);
		final Stream<IIntegrationInforContainer> expContainer = express == null ? 
			Stream.empty() : Stream.of(this.expressionAnalyzer.apply(express));
		IMergable container = Stream.concat(arguments.stream(), expContainer).
			collect(Collectors.reducing(IMergable::merge)).get(); 
		return (IIntegrationInforContainer) container.merge(createInforContainer
			(methodName, null));
	}
}
