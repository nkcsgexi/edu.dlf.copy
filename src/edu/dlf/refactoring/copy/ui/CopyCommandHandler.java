package edu.dlf.refactoring.copy.ui;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.dom.ASTNode;

import edu.dlf.refactoring.copy.Design.ICodeSnippetBuilder;
import edu.dlf.refactoring.copy.Design.IContextualInfoCollector;
import edu.dlf.refactoring.copy.Design.IContextualInfoContainer;
import edu.dlf.refactoring.copy.Design.IIntegrationInforContainer;
import edu.dlf.refactoring.copy.AstAnalyzer;
import edu.dlf.refactoring.copy.ServiceLocator;

public class CopyCommandHandler extends AbstractHandler {

	private final AstAnalyzer analyzer = new AstAnalyzer() {};
	private final ICodeSnippetBuilder builder = ServiceLocator.ResolveType(
		ICodeSnippetBuilder.class);
	private final IContextualInfoCollector collector = ServiceLocator.ResolveType
		(IContextualInfoCollector.class);
	private final Logger logger = ServiceLocator.ResolveType(Logger.class);
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try{
			runTests();
		}catch(Exception e) {
			Assert.isTrue(false);
		}
		return null;
	}

	private void runTests() throws Exception{
		testSimple1();
		testSimple2();
		testSimple3();
		testSimple4();
		testSimple5();
		logger.info("Test done!");
	}
	
	private void testSimple1() {
		IIntegrationInforContainer snippet = builder.apply("method(d);");
		Assert.isTrue(snippet.getImportClues().count() == 1);
		Assert.isTrue(snippet.getToAdaptNodes().count() == 1);
		Assert.isTrue(snippet.getToAdaptNodes().findFirst().get().getNode().
			toString().equals("d"));
	}
	
	private void testSimple2() {
		IIntegrationInforContainer snippet = builder.apply("int d;");
		Assert.isTrue(snippet.getToAdaptNodes().count() == 1);
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
	
	private void testSimple4() {
		IIntegrationInforContainer snippet = builder.apply("MyType a;");
		Assert.isTrue(snippet.getToAdaptNodes().count() == 1);
		Assert.isTrue(snippet.getToAdaptNodes().findFirst().get().getNode().
			toString().equals("a"));
		Assert.isTrue(snippet.getImportClues().count() == 1);
		Assert.isTrue(snippet.getImportClues().findFirst().get().getNode().
			toString().equals("MyType"));
	}
	
	private void testSimple5() throws Exception {
		String code = FileUtils.readFileToString(new File("/home/xige/workspace/"
			+ "edu.dlf.refactoring.copy/src/edu/dlf/refactoring/copy/ui/"
				+ "CopyCommandHandler.java"));
		ASTNode root = analyzer.parseICompilationUnit(code);
		IContextualInfoContainer container = collector.apply(root);
		Assert.isTrue(container.getUnusedNodes().count() > 0);
		Assert.isTrue(container.getUnusedNodes().filter(p -> p.getName().toString().
			equals("event")).count() == 1);
		Assert.isTrue(container.getUnusedNodes().filter(p -> p.getName().toString().
			equals("e")).count() == 1);
		Assert.isTrue(container.getUnusedNodes().filter(p -> p.getName().toString().
			equals("eventwhichdoesnotexist")).count() == 0);
	}
}
