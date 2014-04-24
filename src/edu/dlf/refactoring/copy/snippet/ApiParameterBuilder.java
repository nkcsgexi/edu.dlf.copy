package edu.dlf.refactoring.copy.snippet;

import java.util.function.Predicate;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.ASTNode;

import com.google.inject.Inject;

import edu.dlf.refactoring.copy.Design.IApiParameter;
import edu.dlf.refactoring.copy.Design.IApiParameterBuilder;
import edu.dlf.refactoring.copy.Design.IParameterReplacableTester;

public class ApiParameterBuilder implements IApiParameterBuilder{
	
	private final Predicate<ASTNode> isReplacable;
	private final Logger logger;

	@Inject
	public ApiParameterBuilder(Logger logger, IParameterReplacableTester 
			isReplacable) {
		this.logger = logger;
		this.isReplacable = isReplacable;
	}
	
	@Override
	public IApiParameter apply(final ASTNode node) {
		return new IApiParameter() {
			@Override
			public String get() {
				return node.toString();
			}
			@Override
			public boolean isReplacable() {
				return isReplacable.test(node);
			}};
	}
}
