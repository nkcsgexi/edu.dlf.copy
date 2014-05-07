package edu.dlf.refactoring.copy.snippet;

import java.util.stream.Stream;

import org.eclipse.jdt.core.dom.ASTNode;

import edu.dlf.refactoring.copy.AstAnalyzer;
import edu.dlf.refactoring.copy.Design.IImportClue;
import edu.dlf.refactoring.copy.Design.IIntegrationInforCollector;
import edu.dlf.refactoring.copy.Design.IIntegrationInforContainer;
import edu.dlf.refactoring.copy.Design.IToAdaptNode;

abstract class AbstractIntegrationInforCollector extends AstAnalyzer implements 
		IIntegrationInforCollector{
	protected IIntegrationInforContainer createInforContainer(ASTNode clue, 
			ASTNode adaptNode) {
		return new NullIntegrationInforContainer() {
			@Override
			public Stream<IToAdaptNode> getToAdaptNodes() {
				return adaptNode == null ? Stream.empty() : Stream.of(
					new IToAdaptNode(){
					@Override
					public ASTNode getNode() {
						return adaptNode;
					}});
			}
			@Override
			public Stream<IImportClue> getImportClues() {
				return clue == null ? Stream.empty() : Stream.of(
					new IImportClue(){
					@Override
					public ASTNode getNode() {
						return clue;
					}});
			}};
		}
}
