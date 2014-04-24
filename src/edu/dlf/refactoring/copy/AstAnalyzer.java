package edu.dlf.refactoring.copy;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;

public abstract class AstAnalyzer {

	protected ASTNode parseICompilationUnit(String code) {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(code.toCharArray());
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		ASTNode root = parser.createAST(null);
		return root;
	}

	protected ASTNode parseStatements(String source) {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_STATEMENTS);
		parser.setSource(source.toCharArray());
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		ASTNode root = parser.createAST(null);
		return root;
	}

	protected ASTNode parseExpression(String source) {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_EXPRESSION);
		parser.setSource(source.toCharArray());
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		ASTNode root = parser.createAST(null);
		return root;
	}
	
	protected Stream<ASTNode> getChildren(ASTNode parent) {
		NodeCollectionVisitor visitor = new NodeCollectionVisitor(n -> 
			n.getParent() == parent);
		parent.accept(visitor);
		return visitor.nodes.stream();
	}
	
	protected Stream<ASTNode> getDescendants(ASTNode root, Predicate<ASTNode> 
		condition) {
		NodeCollectionVisitor visitor = new NodeCollectionVisitor(condition);
		root.accept(visitor);
		return visitor.nodes.stream();
	}
	
	protected Stream<ASTNode> getAncestors(ASTNode node, Predicate<ASTNode> 
			condition) {
		Builder<ASTNode> ancestors = Stream.builder();
		for(node = node.getParent(); node != null;node = node.getParent()) {
			if(condition.test(node)) {
				ancestors.accept(node);
			}
		}
		return ancestors.build();	
	}
	 
	private static class NodeCollectionVisitor extends ASTVisitor {
		private final ArrayList<ASTNode> nodes = new ArrayList<ASTNode>();
		private final Predicate<ASTNode> condition;
		
		private NodeCollectionVisitor(Predicate<ASTNode> condition) {
			this.condition = condition;
		}
		public void preVisit(ASTNode node) {
			if(condition.test(node))
				nodes.add(node);
		}
	}	
	protected Function<ASTNode, ASTNode> getStructuralNode(
			final StructuralPropertyDescriptor descriptor) {
		return new Function<ASTNode, ASTNode>() {
			@Override
			public ASTNode apply(ASTNode parent) {
				Object result = parent.getStructuralProperty(descriptor);
				return (ASTNode)result;
		}};
	}
	
	protected Function<ASTNode, Stream<ASTNode>> getStructuralNodeList(
			final StructuralPropertyDescriptor descriptor) {
		return new Function<ASTNode, Stream<ASTNode>>() {
			@Override
			public Stream<ASTNode> apply(ASTNode parent) {
				Object result = parent.getStructuralProperty(descriptor);
				if(result instanceof List) {
					return ((List<ASTNode>)result).stream();
				}else {
					return Stream.of((ASTNode)result);
				}
		}};
	}
}
