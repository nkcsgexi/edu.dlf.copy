package edu.dlf.refactoring.copy.ui;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Assert;

import edu.dlf.refactoring.copy.Design.ICodeSnippetBuilder;
import edu.dlf.refactoring.copy.Design.IIntegrationInforContainer;
import edu.dlf.refactoring.copy.ServiceLocator;

public class CopyCommandHandler extends AbstractHandler {

	private final ICodeSnippetBuilder builder = ServiceLocator.ResolveType(
			ICodeSnippetBuilder.class);
	private final Logger logger = ServiceLocator.ResolveType(Logger.class);
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		testSimple1();
		testSimple2();
		testSimple3();
		return null;
	}
	
	private void testSimple1() {
		IIntegrationInforContainer snippet = builder.apply("method(d);");
		logger.info(snippet.getImportClues().count());
		Assert.isTrue(snippet.getImportClues().count() == 1);
		Assert.isTrue(snippet.getToAdaptNodes().count() == 1);
		Assert.isTrue(snippet.getToAdaptNodes().findFirst().get().getNode().
			toString().equals("d"));
	}
	
	private void testSimple2() {
		IIntegrationInforContainer snippet = builder.apply("int d;");
		Assert.isTrue(snippet.getToAdaptNodes().count()== 1);
		Assert.isTrue(snippet.getToAdaptNodes().findFirst().get().getNode().
			toString().equals("d"));
		Assert.isTrue(snippet.getImportClues().count() == 0);
	}
	
	private void testSimple3() {
		IIntegrationInforContainer snippet = builder.apply("System.out.println(n);");
		Assert.isTrue(snippet.getToAdaptNodes().count() == 1);
		Assert.isTrue(snippet.getToAdaptNodes().findFirst().get().getNode().
			toString().equals("n"));
		Assert.isTrue(snippet.getImportClues().count() == 2);
		Assert.isTrue(snippet.getImportClues().anyMatch(c -> c.getNode().
			toString().equals("println")));
		Assert.isTrue(snippet.getImportClues().anyMatch(c -> c.getNode().
			toString().equals("System.out")));
	}
	
}
