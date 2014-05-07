package edu.dlf.refactoring.copy.snippet;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.core.runtime.Assert;

import edu.dlf.refactoring.copy.Design.IImportClue;
import edu.dlf.refactoring.copy.Design.IIntegrationInforContainer;
import edu.dlf.refactoring.copy.Design.IMergable;
import edu.dlf.refactoring.copy.Design.IToAdaptNode;

public class NullIntegrationInforContainer implements 
		IIntegrationInforContainer {

	@Override
	public final IMergable merge(IMergable an) {
		Assert.isTrue(an instanceof IIntegrationInforContainer);
		IIntegrationInforContainer another = (IIntegrationInforContainer) an;
		final Set<IToAdaptNode> allNodes = Stream.concat(getToAdaptNodes(), another.
			getToAdaptNodes()).collect(Collectors.toSet());
		final Set<IImportClue> allClues = Stream.concat(getImportClues(), another.
			getImportClues()).collect(Collectors.toSet());
		return new NullIntegrationInforContainer(){
			@Override
			public Stream<IToAdaptNode> getToAdaptNodes() {
				return allNodes.stream();
			}
			@Override
			public Stream<IImportClue> getImportClues() {
				return allClues.stream();
		}};
	}

	@Override
	public Stream<IToAdaptNode> getToAdaptNodes() {
		return Stream.empty();
	}

	@Override
	public Stream<IImportClue> getImportClues() {
		return Stream.empty();
	}
}
