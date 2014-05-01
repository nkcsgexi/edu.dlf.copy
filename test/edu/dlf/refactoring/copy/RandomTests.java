package edu.dlf.refactoring.copy;

import java.util.stream.Stream;

import org.eclipse.core.runtime.Assert;
import org.junit.Test;

import edu.dlf.refactoring.copy.Design.ISearchQuery;
import edu.dlf.refactoring.copy.Design.ISearchResult;
import edu.dlf.refactoring.copy.Design.ISearchable;

public class RandomTests {
	@Test
	public void testSearchAllElements() {		
		ISearchable repo = ServiceLocator.ResolveType(ISearchable.class, "binary");
		Stream<ISearchResult> results = repo.search(new ISearchQuery() {
			@Override
			public String getQueryString() {
				return ".+";
			}
		});
		Assert.isTrue(results.count() > 0);
	}
}
