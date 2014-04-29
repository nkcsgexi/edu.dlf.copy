package edu.dlf.refactoring.copy.jar;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Stream;

import org.objectweb.asm.MethodVisitor;

import edu.dlf.refactoring.copy.Design.IBinaryClass;
import edu.dlf.refactoring.copy.Design.IBinaryField;
import edu.dlf.refactoring.copy.Design.IBinaryMethod;

public class ClassStructureVisitor extends AbstractClassVisitor{

	private final ArrayList<String> methodNames = new ArrayList<String>();
	private String className; 
	
	@Override
	public void visit(int version, int access, String name, String signature,
			String superName, String[] interfaces) {
		this.className = name;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc,
			String signature, String[] exceptions) {
		this.methodNames.add(name);
		return null;
	}

	@Override
	public Object getVisitResult() {
		return new IBinaryClass(){
			@Override
			public Stream<IBinaryMethod> getMethods() {
				return methodNames.stream().map(new Function<String, 
					IBinaryMethod>(){
					@Override
					public IBinaryMethod apply(String name) {
						return new IBinaryMethod() {
							@Override
							public String getName() {
								return name;
			}};}});}
			
			@Override
			public Stream<IBinaryField> getFields() {
				return Stream.empty();
			}
			
			@Override
			public String getName() {
				return className;
			}};
	}
}
