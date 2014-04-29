package edu.dlf.refactoring.copy;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.eclipse.jdt.core.dom.ASTNode;

public class Design {
	
	// General design
	public interface INamable {String getName();}
	
	
	// From now on, design for copied snippets
	public interface ICodeSnippetBuilder extends Function<String, ICodeSnippet>{
		
	}
	
	public interface IApiCallBuilder extends Function<ASTNode, IApiCall>{
		
	}
	
	public interface IApiParameterBuilder extends Function<ASTNode, 
		IApiParameter> {
	}
	
	public interface IDeclaredVariableBuilder extends BiFunction<ASTNode, ASTNode, 
		ISnippetDeclaredVariable> {
		
	}
	
	public interface ICodeSnippet {
		Stream<IApiCall> getAllCalls();
		Stream<ISnippetDeclaredVariable> getDeclaredVariables();
	}
	
	public interface IApiCall {
		Stream<IApiParameter> getAllParameters();
	}
	
	public interface IApiMethodName {
		
	}
	
	public interface ISnippetDeclaredVariable extends INamable{
		String getType();
	}
	
	public interface IApiParameter extends Supplier<String>{
		boolean isReplacable();
	}
	
	public interface IReplacableTester extends Predicate<ASTNode> {
		
	}
	
	public interface IParameterReplacableTester extends IReplacableTester{
		
	}
	// From now on, all for jar analysis
	public interface IBinaryClass extends INamable{ 
		Stream<IBinaryMethod> getMethods(); 
		Stream<IBinaryField> getFields();
	}
	public interface IBinaryMethod extends INamable{}
	public interface IBinaryField extends INamable{}
	
}
