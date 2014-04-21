package edu.dlf.refactoring.copy;

import java.util.function.Function;
import java.util.stream.Stream;

import org.eclipse.jdt.core.dom.ASTNode;

public class Design {
	
	public interface ICodeSnippetBuilder extends Function<String, ICodeSnippet>{
		
	}
	
	public interface IApiCallBuilder extends Function<ASTNode, IApiCall>{
		
	}
	
	public interface ICodeSnippet {
		Stream<IApiCall> getAllCalls();
	}
	
	public interface IApiCall {
		Stream<IApiParameter> getAllParameters();
	}
	
	public interface IApiParameter {
		
	}
}
