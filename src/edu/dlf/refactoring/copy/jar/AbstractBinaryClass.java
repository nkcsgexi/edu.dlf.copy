package edu.dlf.refactoring.copy.jar;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import edu.dlf.refactoring.copy.Design.IBinaryClass;
import edu.dlf.refactoring.copy.Design.IBinaryField;
import edu.dlf.refactoring.copy.Design.IBinaryMethod;
import edu.dlf.refactoring.copy.Design.IMergable;
import edu.dlf.refactoring.copy.Design.ISearchQuery;
import edu.dlf.refactoring.copy.Design.ISearchResult;

public abstract class AbstractBinaryClass implements IBinaryClass{
	
	@Override
	public Stream<ISearchResult> search(ISearchQuery query) {
		ArrayList<ISearchResult> results = new ArrayList<ISearchResult>();
		if(this.getName().matches(query.getQueryString())) {
			results.add(this);
		}
		this.getMethods().filter(m -> m.getName().matches(query.getQueryString())).
			forEach(results::add);
		this.getFields().filter(f -> f.getName().matches(query.getQueryString())).
			forEach(results::add);
		return results.stream();
	}
	
	@Override
	public IMergable merge(IMergable another) {
		if(another instanceof IBinaryClass) {
			final IBinaryClass anotherClass = (IBinaryClass) another;
			if(anotherClass.getName().equals(this.getName())) {
				String commonName = this.getName();
				List<IBinaryMethod> allMethods = this.getMethods().collect
					(Collectors.toList());
				allMethods.addAll(anotherClass.getMethods().collect(Collectors.
					toList()));
				List<IBinaryField> allFields = this.getFields().collect
					(Collectors.toList());
				allFields.addAll(anotherClass.getFields().collect(Collectors.
					toList()));	
				final Stream<IBinaryMethod> methodStream = allMethods.stream();
				final Stream<IBinaryField> fieldStream = allFields.stream();
				return new AbstractBinaryClass() {
					@Override
					public String getName() {
						return commonName;
					}
					@Override
					public Stream<IBinaryMethod> getMethods() {
						return methodStream;
					}
					@Override
					public Stream<IBinaryField> getFields() {
						return fieldStream;
					}};
		}}
		return this;
	}
}
