package edu.dlf.refactoring.copy.ui;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.dom.ASTNode;

import edu.dlf.refactoring.copy.Design.ICodeSnippet;
import edu.dlf.refactoring.copy.Design.ICodeSnippetBuilder;
import edu.dlf.refactoring.copy.ServiceLocator;

public class CopyCommandHandler extends AbstractHandler {

	private final ICodeSnippetBuilder builder = ServiceLocator.ResolveType(
			ICodeSnippetBuilder.class);
	private final Logger logger = ServiceLocator.ResolveType(Logger.class);
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		testSimple1();
		return null;
	}
	
	private void testSimple1() {
		ICodeSnippet snippet = builder.apply("method(d);");
		List<ASTNode> list = snippet.get().collect(Collectors.toList());
		Assert.isTrue(list.size() == 1);
		Assert.isTrue(list.get(0).toString().equals("d"));
	}
}
