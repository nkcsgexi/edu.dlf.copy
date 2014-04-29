package edu.dlf.refactoring.copy.jar;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.EmptyVisitor;

public abstract class AbstractClassVisitor implements ClassVisitor{

	private final EmptyVisitor nullVisitor = new EmptyVisitor();
	
	protected EmptyVisitor getNullVisitor() {
		return this.nullVisitor;
	}
	
	public abstract void visit(int version, int access, String name, String signature,
		String superName, String[] interfaces);
	
	public abstract MethodVisitor visitMethod(int access, String name, String desc, 
		String signature, String[] exceptions);
	
	public abstract Object getVisitResult();
	
	@Override
	public AnnotationVisitor visitAnnotation(String arg0, boolean arg1) {
		return null;
	}

	@Override
	public void visitAttribute(Attribute attr) {
	}

	@Override
	public void visitEnd() {
	}

	@Override
	public FieldVisitor visitField(int access, String name, String desc, String 
			signature, Object value)  {
		return null;
	}

	@Override
	public void visitInnerClass(String name, String outerName, String innerName, 
		int access)  {
	}

	@Override
	public void visitOuterClass(String owner, String name, String desc) {
	}

	@Override
	public void visitSource(String source, String debug)  {
	}
}
