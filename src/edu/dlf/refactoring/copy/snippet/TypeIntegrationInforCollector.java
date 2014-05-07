package edu.dlf.refactoring.copy.snippet;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.ASTNode;

import com.google.inject.Inject;

import edu.dlf.refactoring.copy.Design.IIntegrationInforContainer;

public class TypeIntegrationInforCollector extends AbstractIntegrationInforCollector{
		
	private final Logger logger;

	@Inject
	public TypeIntegrationInforCollector(Logger logger) {
		this.logger = logger;
	}
	
	@Override
	public IIntegrationInforContainer apply(ASTNode type) {
		if(type.getNodeType() == ASTNode.SIMPLE_TYPE || type.getNodeType() == 
			ASTNode.QUALIFIED_TYPE) {
			return createInforContainer(type, null);
		}
		return createInforContainer(null, null);
	}

}
