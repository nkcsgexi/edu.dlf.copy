package edu.dlf.refactoring.copy.jar;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import edu.dlf.refactoring.copy.Design.IBinaryClass;
import edu.dlf.refactoring.copy.Design.IBinaryField;
import edu.dlf.refactoring.copy.Design.IBinaryMethod;
import edu.dlf.refactoring.copy.Design.IMergable;

public abstract class AbstractBinaryClass implements IBinaryClass{
	@Override
	public IMergable merge(IMergable o) {
		if(o instanceof IBinaryClass) {
			final IBinaryClass another = (IBinaryClass) o;
			if(another.getName().equals(this.getName())) {
				String commonName = this.getName();
				List<IBinaryMethod> allMethods = this.getMethods().collect
					(Collectors.toList());
				allMethods.addAll(another.getMethods().collect(Collectors.toList()));
				List<IBinaryField> allFields = this.getFields().collect
					(Collectors.toList());
				allFields.addAll(another.getFields().collect(Collectors.toList()));	
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
