package edu.dlf.refactoring.copy.jar;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import edu.dlf.refactoring.copy.Design.IBinaryClass;
import edu.dlf.refactoring.copy.Design.IBinaryMethod;

public class JarAnalyzer {
	
	protected Stream<String> getClassNames(String path) throws Exception {
		List<String> classNames=new ArrayList<String>();
		ZipInputStream zip=new ZipInputStream(new FileInputStream(path));
		for(ZipEntry entry=zip.getNextEntry();entry!=null;entry=zip.
				getNextEntry()) {   
			if(entry.getName().endsWith(".class") && !entry.isDirectory()) {
				StringBuilder className=new StringBuilder();
				for(String part : entry.getName().split("/")) {
					if(className.length() != 0)
						className.append(".");
					className.append(part);
					if(part.endsWith(".class"))
						className.setLength(className.length()-".class".length());
				}
				classNames.add(className.toString());
		}}
		zip.close();
		return classNames.stream();
	}
	
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
		JarAnalyzer instance = new JarAnalyzer();
		Stream<IBinaryClass> classes = instance.readJarFile("/home/xige/"
				+ "workspace/edu.dlf.refactoring.copy/lib/log4j-1.2.17.jar");
		classes.forEach(c -> {
			Stream<IBinaryMethod> methods = c.getMethods();
			methods.forEach(m -> System.out.println(m.getName()));
		});
	}
}
