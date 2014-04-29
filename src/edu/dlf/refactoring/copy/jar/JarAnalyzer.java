package edu.dlf.refactoring.copy.jar;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import edu.dlf.refactoring.copy.Design.IBinaryClass;
import edu.dlf.refactoring.copy.Design.IBinaryMethod;

public abstract class JarAnalyzer {

	protected Stream<IBinaryClass> readJarFile(String path) throws Exception {
		List<IBinaryClass> allClasses = new ArrayList<IBinaryClass>();
		JarFile file = new JarFile(new File(path));
		Enumeration<JarEntry> entries = file.entries();
	    while (entries.hasMoreElements()) {
	        JarEntry entry = entries.nextElement();
	        String entryName = entry.getName();
	        if (entryName.endsWith(".class")) {
	            ClassNode classNode = new ClassNode();
	            InputStream classFileInputStream = file.getInputStream(entry);
		            try {
		                ClassReader classReader = new ClassReader(
		                	classFileInputStream);
		                classReader.accept(classNode, 0);
		            } finally {
		                classFileInputStream.close();
		            }
		            allClasses.add(createBinaryClass(classNode));
		        }
		 }
	    file.close();
	    return allClasses.stream();
	}
	
	private IBinaryClass createBinaryClass(ClassNode classNode) {
		AbstractClassVisitor visitor = new ClassStructureVisitor();
		classNode.accept(visitor);
		return (IBinaryClass)visitor.getVisitResult();
	}

	public static void main(String[] args) throws Exception {
		JarAnalyzer instance = new JarAnalyzer(){};
		Stream<IBinaryClass> classes = instance.readJarFile("/home/xige/"
				+ "workspace/edu.dlf.refactoring.copy/lib/log4j-1.2.17.jar");
		classes.forEach(c -> {
			Stream<IBinaryMethod> methods = c.getMethods();
			System.out.println(c.getName());
			//methods.forEach(m -> System.out.println(m.getName()));
		});
	}
}
