package edu.dlf.refactoring.copy.snippet;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import edu.dlf.refactoring.copy.Design.IIntegrationInforCollector;
import edu.dlf.refactoring.copy.Design.IIntegrationInforContainer;
import edu.dlf.refactoring.copy.Design.IMergable;


public class VariableDeclarationCollector extends AbstractIntegrationInforCollector{

	private final Logger logger;
	private final Function<ASTNode, Stream<ASTNode>> getDeclaredNamesFunc;
	private final Function<ASTNode, Stream<ASTNode>> getInitializersFunc;
	private final Function<ASTNode, Stream<ASTNode>> getTypeFunc;
	private final IIntegrationInforCollector expressionCollector;
	private final IIntegrationInforCollector typeCollector;
	
	@Inject
	public VariableDeclarationCollector(
			Logger logger, 
			@Named("expression") IIntegrationInforCollector expressionCollector,
			@Named("type") IIntegrationInforCollector typeCollector) {
		this.logger = logger;
		this.expressionCollector = expressionCollector;
		this.typeCollector = typeCollector;
		this.getDeclaredNamesFunc = this.getStructuralNodeList(
			VariableDeclarationStatement.FRAGMENTS_PROPERTY).andThen(s -> s.map
				(getStructuralNode(VariableDeclarationFragment.NAME_PROPERTY)));
		this.getInitializersFunc = this.getStructuralNodeList(
			VariableDeclarationStatement.FRAGMENTS_PROPERTY).andThen(s -> s.map
				(getStructuralNode(VariableDeclarationFragment.
					INITIALIZER_PROPERTY)));
		this.getTypeFunc = this.getStructuralNodeList(VariableDeclarationStatement.
			TYPE_PROPERTY);
	}

	@Override
	public IIntegrationInforContainer apply(ASTNode variableStatement) {
		Stream<ASTNode> allNames = getDeclaredNamesFunc.apply(variableStatement);
		IIntegrationInforContainer allInits = (IIntegrationInforContainer) 
			getInitializersFunc.apply(variableStatement).map(expressionCollector).
				collect(Collectors.reducing(IMergable::merge)).get();
		IIntegrationInforContainer type = (IIntegrationInforContainer) getTypeFunc.apply
			(variableStatement).map(typeCollector).collect(Collectors.reducing
				(IMergable::merge)).get();
		return (IIntegrationInforContainer) allNames.map(n -> createInforContainer
			(null, n)).collect(Collectors.reducing(IMergable::merge)).get().merge
				(allInits).merge(type);
	}
}
