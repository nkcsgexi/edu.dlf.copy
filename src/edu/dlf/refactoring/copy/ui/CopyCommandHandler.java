package edu.dlf.refactoring.copy.ui;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import edu.dlf.refactoring.copy.Design.ICodeSnippet;
import edu.dlf.refactoring.copy.Design.ICodeSnippetBuilder;
import edu.dlf.refactoring.copy.ServiceLocator;

public class CopyCommandHandler extends AbstractHandler {

	private final ICodeSnippetBuilder builder = ServiceLocator.ResolveType(
			ICodeSnippetBuilder.class);
	private final Logger logger = ServiceLocator.ResolveType(Logger.class);
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ICodeSnippet snippet = builder.apply("method(d);");
		String text = snippet.getAllCalls().findFirst().get().getAllParameters().
			findFirst().get().get();
		logger.info(text);
		return null;
	}
}
