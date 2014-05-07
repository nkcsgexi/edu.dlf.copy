package edu.dlf.refactoring.copy;

import java.util.function.Function;
import java.util.stream.Stream;

import org.eclipse.jdt.core.dom.ASTNode;

public class Design {
	
	// General design
	public interface INamable {String getName();}
	public interface IMergable {IMergable merge(IMergable another);}
	
	public interface ISearchable {Stream<ISearchResult> search(ISearchQuery query);}
	public interface ISearchQuery {String getQueryString();}
	public interface ISearchResult {}
	public interface IASTNodeContainer{ ASTNode getNode();}
	
	// From now on, design for copied snippets
	public interface ICodeSnippetBuilder extends Function<String, 
		IIntegrationInforContainer>{}
	
	public interface IToAdaptNode extends IASTNodeContainer{}
	
	public interface IImportClue extends IASTNodeContainer{}
	
	public interface IIntegrationInforContainer extends IMergable{
		Stream<IToAdaptNode> getToAdaptNodes();
		Stream<IImportClue> getImportClues();
	}
	
	public interface IIntegrationInforCollector extends Function<ASTNode, 
		IIntegrationInforContainer>{};
		
	// From now on, all for jar analysis
	public interface IBinaryClass extends INamable, IMergable, ISearchable, 
			ISearchResult{ 
		Stream<IBinaryMethod> getMethods(); 
		Stream<IBinaryField> getFields();
	}
	
	public interface IBinaryMethod extends INamable, ISearchResult{}
	public interface IBinaryField extends INamable, ISearchResult{}
	
}
