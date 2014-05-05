package edu.dlf.refactoring.copy;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.eclipse.jdt.core.dom.ASTNode;

public class Design {
	
	// General design
	public interface INamable {String getName();}
	public interface IMergable {IMergable merge(IMergable another);}
	
	public interface ISearchable {Stream<ISearchResult> search(ISearchQuery query);}
	public interface ISearchQuery {String getQueryString();}
	public interface ISearchResult {}
	
	// From now on, design for copied snippets
	public interface ICodeSnippetBuilder extends Function<String, ICodeSnippet>{}
	
	public interface IApiCallBuilder extends Function<ASTNode, IApiCall>{}
	
	public interface IUpdatedNodesContainer extends Supplier<Stream<ASTNode>> {}
	
	public interface IExpressionUpdatedNodesCollecter extends 
		Function<ASTNode, Stream<ASTNode>>{}
	
	public interface IApiParameterBuilder extends Function<ASTNode, 
		IApiParameter> {}
	
	public interface IDeclaredVariableBuilder extends BiFunction<ASTNode, ASTNode, 
		ISnippetDeclaredVariable> {}
	
	////
	public interface ICodeSnippet extends IUpdatedNodesContainer{
		Stream<IApiCall> getAllCalls();
		Stream<ISnippetDeclaredVariable> getDeclaredVariables();
	}
	
	public interface IApiCall extends IUpdatedNodesContainer{
		Stream<IApiParameter> getAllParameters();
	}
	
	public interface IApiParameter extends IUpdatedNodesContainer{}
	
	public interface ISnippetDeclaredVariable extends INamable{
		String getType();
	}
	
	// From now on, all for jar analysis
	public interface IBinaryClass extends INamable, IMergable, ISearchable, 
			ISearchResult{ 
		Stream<IBinaryMethod> getMethods(); 
		Stream<IBinaryField> getFields();
	}
	public interface IBinaryMethod extends INamable, ISearchResult{}
	public interface IBinaryField extends INamable, ISearchResult{}
	
}
