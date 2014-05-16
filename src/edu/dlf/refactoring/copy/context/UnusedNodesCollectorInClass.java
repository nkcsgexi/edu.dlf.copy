package edu.dlf.refactoring.copy.context;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import com.google.inject.Inject;

import edu.dlf.refactoring.copy.AstAnalyzer;
import edu.dlf.refactoring.copy.Design.IContextualInfoCollector;
import edu.dlf.refactoring.copy.Design.IContextualInfoContainer;
import edu.dlf.refactoring.copy.Design.ITypeNamePair;

public class UnusedNodesCollectorInClass extends AstAnalyzer implements 
		IContextualInfoCollector {
	
	private final Logger logger;
	private final Function<Stream<ASTNode>, Stream<ASTNode>> getFragmentNames;
	private final Function<ASTNode, Stream<ITypeNamePair>> collectExpression;
	private final Function<ASTNode, Stream<ITypeNamePair>> collectStatement;
	private final Function<ASTNode, Stream<ITypeNamePair>> collectSingleVariable;
	
	@Inject
	public UnusedNodesCollectorInClass(Logger logger) {
		this.logger = logger;
		this.getFragmentNames = getStructuralNodeListFromList(
			VariableDeclarationFragment.NAME_PROPERTY);
		this.collectExpression = collectPairs(getStructuralNode(
			VariableDeclarationExpression.TYPE_PROPERTY), getStructuralNodeList
				(VariableDeclarationExpression.FRAGMENTS_PROPERTY).andThen(
					getFragmentNames));
		this.collectStatement = collectPairs(getStructuralNode(
			VariableDeclarationStatement.TYPE_PROPERTY), getStructuralNodeList
				(VariableDeclarationStatement.FRAGMENTS_PROPERTY).andThen(
						getFragmentNames));
		this.collectSingleVariable = collectPairs(getStructuralNode(
			SingleVariableDeclaration.TYPE_PROPERTY), getStructuralNodeList
				(SingleVariableDeclaration.NAME_PROPERTY));
	}
	
	private Function<ASTNode, Stream<ITypeNamePair>> collectPairs(Function<ASTNode, 
		ASTNode> getType, Function<ASTNode, Stream<ASTNode>> getNames) {
		return new Function<ASTNode, Stream<ITypeNamePair>>() {
			@Override
			public Stream<ITypeNamePair> apply(ASTNode node) {
				final ASTNode type = getType.apply(node);
				Stream<ASTNode> names = getNames.apply(node);
				return names.map(name -> (ITypeNamePair) new ITypeNamePair(){
					@Override
					public ASTNode getType() {
						return type;
					}
					@Override
					public ASTNode getName() {
						return name;
					}});
		}};
	}
	
	@Override
	public IContextualInfoContainer apply(ASTNode root) {
		Stream<ITypeNamePair> expressionPairs = this.getDescendants
			(root, n -> n.getNodeType() == ASTNode.
				VARIABLE_DECLARATION_EXPRESSION).flatMap(collectExpression);
		Stream<ITypeNamePair> statePairs = this.getDescendants
			(root, n -> n.getNodeType() == ASTNode.
				VARIABLE_DECLARATION_STATEMENT).flatMap(collectStatement);
		Stream<ITypeNamePair> singleVariablePairs = this.getDescendants
			(root, n -> n.getNodeType() == ASTNode.
				SINGLE_VARIABLE_DECLARATION).flatMap(collectSingleVariable);
		List<ITypeNamePair> list = Stream.concat(Stream.concat(expressionPairs, 
			statePairs), singleVariablePairs).collect(Collectors.toList());
		return new IContextualInfoContainer() {
			@Override
			public Stream<ITypeNamePair> getUnusedNodes() {
				return list.stream();
		}};
	}
}










