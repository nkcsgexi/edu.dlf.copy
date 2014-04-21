package edu.dlf.refactoring.copy.snippet;

import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.ASTNode;

import com.google.inject.Inject;

import edu.dlf.refactoring.copy.AstAnalyzer;
import edu.dlf.refactoring.copy.Design.IApiCall;
import edu.dlf.refactoring.copy.Design.IApiCallBuilder;
import edu.dlf.refactoring.copy.Design.ICodeSnippet;
import edu.dlf.refactoring.copy.Design.ICodeSnippetBuilder;

public class CodeSnippetBuilder extends AstAnalyzer implements ICodeSnippetBuilder{

	private final IApiCallBuilder callBuilder;
	private final Logger logger;

	@Inject
	public CodeSnippetBuilder(Logger logger, IApiCallBuilder callBuilder) {
		this.logger = logger;
		this.callBuilder = callBuilder;
	}
	
	@Override
	public ICodeSnippet apply(String source) {
		ASTNode root = parseStatements(source);
		final Stream<IApiCall> apiCalls = getDescendants(root, node -> node.
			getNodeType() == ASTNode.METHOD_INVOCATION).map(callBuilder);
		return new ICodeSnippet() {
			@Override
			public Stream<IApiCall> getAllCalls() {
				return apiCalls;
			}
		};
	}
}
