package edu.dlf.refactoring.copy.snippet;

import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import com.google.inject.Inject;

import edu.dlf.refactoring.copy.AstAnalyzer;
import edu.dlf.refactoring.copy.Design.IApiCall;
import edu.dlf.refactoring.copy.Design.IApiCallBuilder;
import edu.dlf.refactoring.copy.Design.ICodeSnippet;
import edu.dlf.refactoring.copy.Design.ICodeSnippetBuilder;
import edu.dlf.refactoring.copy.Design.IDeclaredVariableBuilder;
import edu.dlf.refactoring.copy.Design.ISnippetDeclaredVariable;

public class CodeSnippetBuilder extends AstAnalyzer implements ICodeSnippetBuilder{

	private final IApiCallBuilder callBuilder;
	private final Logger logger;
	private final IDeclaredVariableBuilder variableBuilder;
	private final Function<ASTNode, Stream<ASTNode>> getFragmentsFunc;
	private final Function<ASTNode, ASTNode> getNameFunc;

	@Inject
	public CodeSnippetBuilder(Logger logger, 
			IApiCallBuilder callBuilder, 
			IDeclaredVariableBuilder variableBuilder) {
		this.logger = logger;
		this.callBuilder = callBuilder;
		this.variableBuilder = variableBuilder;
		this.getFragmentsFunc = getStructuralNodeList(VariableDeclarationStatement.
			FRAGMENTS_PROPERTY);
		this.getNameFunc = getStructuralNode(VariableDeclarationFragment.
			NAME_PROPERTY);
	}
	
	@Override
	public ICodeSnippet apply(String source) {
		final ASTNode root = parseStatements(source);
		final Stream<ISnippetDeclaredVariable> variables = getDescendants(root, 
			s -> s.getNodeType() == ASTNode.VARIABLE_DECLARATION_STATEMENT).flatMap
				(this::createVariables);
		final Stream<IApiCall> apiCalls = getDescendants(root, node -> node.
			getNodeType() == ASTNode.METHOD_INVOCATION).map(callBuilder);
		return new ICodeSnippet() {
			@Override
			public Stream<IApiCall> getAllCalls() {
				return apiCalls;
			}
			@Override
			public Stream<ISnippetDeclaredVariable> getDeclaredVariables() {
				return variables;
		}};
	}

	private Stream<ISnippetDeclaredVariable> createVariables(ASTNode s) {
		ASTNode type = (ASTNode)s.getStructuralProperty(VariableDeclarationStatement.
			TYPE_PROPERTY);
		Stream<ASTNode> names = getFragmentsFunc.apply(s).map(getNameFunc);	
		return names.map(name -> variableBuilder.apply(type, name));
	}
}
