package edu.dlf.refactoring.copy.snippet;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.ASTNode;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import edu.dlf.refactoring.copy.AstAnalyzer;
import edu.dlf.refactoring.copy.Design.ICodeSnippetBuilder;
import edu.dlf.refactoring.copy.Design.IIntegrationInforCollector;
import edu.dlf.refactoring.copy.Design.IIntegrationInforContainer;
import edu.dlf.refactoring.copy.Design.IMergable;

public class CodeSnippetBuilder extends AstAnalyzer implements ICodeSnippetBuilder{

	private final Logger logger;
	private final IIntegrationInforCollector callCollector;
	private final IIntegrationInforCollector variableCollector;

	@Inject
	public CodeSnippetBuilder(
			Logger logger, 
			@Named("invocation") IIntegrationInforCollector callCollector, 
			@Named("declaration") IIntegrationInforCollector variableCollector) {
		this.logger = logger;
		this.callCollector = callCollector;
		this.variableCollector = variableCollector;
	}
	
	@Override
	public IIntegrationInforContainer apply(String source) {
		final ASTNode root = parseStatements(source);
		final List<IIntegrationInforContainer> variables = getDescendants(root, 
			s -> s.getNodeType() == ASTNode.VARIABLE_DECLARATION_STATEMENT).map
				(variableCollector).collect(Collectors.toList());
		final List<IIntegrationInforContainer> apiCalls = getDescendants(root, 
			node -> node.getNodeType() == ASTNode.METHOD_INVOCATION).map(
				callCollector).collect(Collectors.toList());
		Optional<IMergable> vContainer = variables.stream().collect(Collectors.
			reducing(IMergable::merge));
		Optional<IMergable> cContainer = apiCalls.stream().collect(Collectors.
			reducing(IMergable::merge));
		IMergable vc = vContainer.isPresent() ? vContainer.get() : 
			new NullIntegrationInforContainer(); 
		IMergable cc = cContainer.isPresent() ? cContainer.get() : 
			new NullIntegrationInforContainer();
		return (IIntegrationInforContainer) vc.merge(cc);
	}
}
