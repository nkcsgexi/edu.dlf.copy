package edu.dlf.refactoring.copy.jar;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.objectweb.asm.MethodVisitor;

import com.google.common.collect.ImmutableList;

import edu.dlf.refactoring.copy.Design.IBinaryField;
import edu.dlf.refactoring.copy.Design.IBinaryMethod;

public class ClassStructureVisitor extends AbstractClassVisitor{

	private final ArrayList<String> methodNames = new ArrayList<String>();
	private final StringBuilder className = new StringBuilder(); 
	
	private static final String suffix = Pattern.quote("$") +"[0-9]+";
	private static final ImmutableList<String> uselessMethodNames = 
		ImmutableList.of("<init>", "<clinit>", "class$");
	
	private String removeSuffix(String name) {
		return name.replaceAll(suffix, "");
	}
	
	@Override
	public void visit(int version, int access, String name, String signature,
			String superName, String[] interfaces) {
		this.className.append(removeSuffix(name).replaceAll(Pattern.quote("/"), "."));
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc,
			String signature, String[] exceptions) {
		name = removeSuffix(name);
		if(!uselessMethodNames.contains(name))
			this.methodNames.add(name);
		return null;
	}

	@Override
	public Object getVisitResult() {
		return new AbstractBinaryClass(){
			@Override
			public Stream<IBinaryMethod> getMethods() {
				return methodNames.stream().distinct().map(new Function<String, 
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
				return className.toString();
			}};
	}
}
